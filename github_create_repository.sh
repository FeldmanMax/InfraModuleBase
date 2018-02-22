#!/usr/bin/env bash
repository_name=$1

echo "# $repository_name" >> README.md
echo "Initialize Repository\n"
git init

echo "Add Files\n"
git add .

echo "Initial Commit\n"
git commit -m "first commit"

echo "Set REMOTE destination\n"
git remote add origin https://github.com/FeldmanMax/${repository_name}.git

echo "Push\n"
git push -u origin master