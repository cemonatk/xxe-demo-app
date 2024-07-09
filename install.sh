#!/bin/bash
set -e

docker build -t xxe-demo-app .

docker run -d -p 8000:8000 --name xxe-demo xxe-demo-app
