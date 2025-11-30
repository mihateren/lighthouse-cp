#!/bin/bash
sleep 10  # Даем LocalStack время на запуск
echo "Initializing S3 bucket in region: ${AWS_DEFAULT_REGION:-us-east-1}"
awslocal s3 mb s3://lighthouse-photos
awslocal s3api put-bucket-acl --bucket lighthouse-photos --acl public-read
echo "S3 bucket 'lighthouse-photos' created successfully in region: ${AWS_DEFAULT_REGION:-us-east-1}!"