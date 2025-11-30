-- liquibase formatted sql
-- changeset admin:1

CREATE TABLE lighthouse.volunteer_centers
(
    center_id bigint PRIMARY KEY,
    title varchar(50) NOT NULL,
    city varchar(50) NOT NULL,
    post_code varchar(6) NOT NULL,
    address varchar(100) NOT NULL
);

CREATE TABLE lighthouse.admins
(
    admin_id bigint PRIMARY KEY,
    center_id bigint NOT NULL,
    first_name varchar(30) NOT NULL,
    last_name varchar(30) NOT NULL,
    email varchar(100) NOT NULL,
    birthdate date NOT NULL,
    CONSTRAINT chk_admin_age CHECK (birthdate <= now() - interval '18 years')
);

CREATE TABLE lighthouse.victims
(
    victim_id bigint PRIMARY KEY,
    first_name varchar(30) NOT NULL,
    last_name varchar(30) NOT NULL,
    city varchar(50) NOT NULL,
    age integer,
    height integer,
    photo text,
    notes text,
    CONSTRAINT chk_victim_age_range CHECK (age > 0 AND age < 150),
    CONSTRAINT chk_victim_height_range CHECK (height > 0 AND height < 300)
);

CREATE TABLE lighthouse.volunteers
(
    volunteer_id bigint PRIMARY KEY,
    center_id bigint NOT NULL,
    first_name varchar(30) NOT NULL,
    last_name varchar(30) NOT NULL,
    birthdate date NOT NULL,
    phone varchar(20) NOT NULL,
    email varchar(100) NOT NULL,
    skills text,
    is_active boolean NOT NULL DEFAULT true,
    registration_date date NOT NULL DEFAULT now(),
    CONSTRAINT chk_volunteer_age CHECK (birthdate <= now() - interval '18 years')
);

CREATE TABLE lighthouse.search_operations
(
    operation_id bigint PRIMARY KEY,
    victim_id bigint NOT NULL,
    admin_id bigint NOT NULL,
    status varchar(20) NOT NULL DEFAULT 'active',
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp NOT NULL DEFAULT now()
);

CREATE TABLE lighthouse.volunteer_teams
(
    team_id bigint PRIMARY KEY,
    operation_id bigint NOT NULL,
    team_leader_id bigint NOT NULL,
    team_name varchar(50) NOT NULL,
    created_at timestamp NOT NULL DEFAULT now()
);

CREATE TABLE lighthouse.team_members
(
    team_id bigint NOT NULL,
    volunteer_id bigint NOT NULL,
    joined_at timestamp NOT NULL DEFAULT now(),
    PRIMARY KEY (team_id, volunteer_id)
);

CREATE TABLE lighthouse.search_diaries
(
    diary_id bigint PRIMARY KEY,
    team_id bigint NOT NULL,
    volunteer_id bigint NOT NULL,
    activity_type varchar(30) NOT NULL,
    description text NOT NULL,
    location varchar(100),
    created_at timestamp NOT NULL DEFAULT now()
);

CREATE TABLE lighthouse.operation_status_updates
(
    update_id bigint PRIMARY KEY,
    operation_id bigint NOT NULL,
    volunteer_id bigint NOT NULL,
    proposed_status varchar(20) NOT NULL,
    reason text,
    admin_decision varchar(10) DEFAULT 'pending',
    admin_notes text,
    created_at timestamp NOT NULL DEFAULT now(),
    decided_at timestamp
);

-- Индексы для volunteer_centers
CREATE INDEX i_centers_city ON lighthouse.volunteer_centers (city);
CREATE INDEX i_centers_postcode ON lighthouse.volunteer_centers (post_code);

-- Индексы для admins
CREATE INDEX i_admins_center_name ON lighthouse.admins (center_id, last_name, first_name);
CREATE UNIQUE INDEX u_admins_email ON lighthouse.admins (email);
CREATE INDEX i_admins_birthdate ON lighthouse.admins (birthdate);

-- Индексы для victims
CREATE INDEX i_victims_city_name ON lighthouse.victims (city, last_name, first_name);
CREATE INDEX i_victims_age ON lighthouse.victims (age);
CREATE INDEX i_victims_height ON lighthouse.victims (height);

-- Индексы для volunteers
CREATE INDEX i_volunteers_center_active ON lighthouse.volunteers (center_id, is_active, registration_date);
CREATE UNIQUE INDEX u_volunteers_email ON lighthouse.volunteers (email);
CREATE INDEX i_volunteers_name ON lighthouse.volunteers (last_name, first_name);
CREATE INDEX i_volunteers_registration ON lighthouse.volunteers (registration_date);

-- Индексы для search_operations
CREATE INDEX i_operations_victim ON lighthouse.search_operations (victim_id);
CREATE INDEX i_operations_admin ON lighthouse.search_operations (admin_id);
CREATE INDEX i_operations_status_date ON lighthouse.search_operations (status, created_at);

-- Индексы для volunteer_teams
CREATE INDEX i_teams_operation ON lighthouse.volunteer_teams (operation_id);
CREATE INDEX i_teams_leader ON lighthouse.volunteer_teams (team_leader_id);

-- Индексы для team_members
CREATE INDEX i_members_volunteer ON lighthouse.team_members (volunteer_id);

-- Индексы для search_diaries
CREATE INDEX i_diaries_team_date ON lighthouse.search_diaries (team_id, created_at);
CREATE INDEX i_diaries_volunteer ON lighthouse.search_diaries (volunteer_id);
CREATE INDEX i_diaries_activity ON lighthouse.search_diaries (activity_type);

-- Индексы для operation_status_updates
CREATE INDEX i_status_operation_decision ON lighthouse.operation_status_updates (operation_id, admin_decision);
CREATE INDEX i_status_volunteer ON lighthouse.operation_status_updates (volunteer_id);
CREATE INDEX i_status_created ON lighthouse.operation_status_updates (created_at);

-- Внешние ключи
ALTER TABLE lighthouse.admins ADD FOREIGN KEY (center_id) REFERENCES lighthouse.volunteer_centers (center_id);
ALTER TABLE lighthouse.volunteers ADD FOREIGN KEY (center_id) REFERENCES lighthouse.volunteer_centers (center_id);
ALTER TABLE lighthouse.search_operations ADD FOREIGN KEY (victim_id) REFERENCES lighthouse.victims (victim_id);
ALTER TABLE lighthouse.search_operations ADD FOREIGN KEY (admin_id) REFERENCES lighthouse.admins (admin_id);
ALTER TABLE lighthouse.volunteer_teams ADD FOREIGN KEY (operation_id) REFERENCES lighthouse.search_operations (operation_id);
ALTER TABLE lighthouse.volunteer_teams ADD FOREIGN KEY (team_leader_id) REFERENCES lighthouse.volunteers (volunteer_id);
ALTER TABLE lighthouse.team_members ADD FOREIGN KEY (team_id) REFERENCES lighthouse.volunteer_teams (team_id);
ALTER TABLE lighthouse.team_members ADD FOREIGN KEY (volunteer_id) REFERENCES lighthouse.volunteers (volunteer_id);
ALTER TABLE lighthouse.search_diaries ADD FOREIGN KEY (team_id) REFERENCES lighthouse.volunteer_teams (team_id);
ALTER TABLE lighthouse.search_diaries ADD FOREIGN KEY (volunteer_id) REFERENCES lighthouse.volunteers (volunteer_id);
ALTER TABLE lighthouse.operation_status_updates ADD FOREIGN KEY (operation_id) REFERENCES lighthouse.search_operations (operation_id);
ALTER TABLE lighthouse.operation_status_updates ADD FOREIGN KEY (volunteer_id) REFERENCES lighthouse.volunteers (volunteer_id);

-- Комментарии
COMMENT ON COLUMN lighthouse.victims.photo IS 'S3 link';
COMMENT ON COLUMN lighthouse.volunteers.skills IS 'Компетенции и навыки волонтера';