#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

SOURCE_BRANCH="master"
TARGET_BRANCH="gh-pages"
GITHUB_USER="ribasco"

# Do not deploy on pull-requests or commits on other branches
if [ "$TRAVIS_PULL_REQUEST" != "false" -o "$TRAVIS_BRANCH" != "$SOURCE_BRANCH" ]; then
    echo "Skipping deploy. Performing build."
    scripts/build-site.sh
    exit 0
fi

# Start Building the Site
scripts/build-site.sh

# Once we are done building, we can start copying the files to the branch

# Clone Github Pages Branch
echo "Extracting latest gh-pages from remote origin"
git clone -b $TARGET_BRANCH --single-branch https://$GITHUB_USER:$GITUB_TOKEN@github.com/$GITHUB_USER/async-gamequery-lib.git $TARGET_BRANCH

# Update Remote Configuration
echo "Updating Remote Origin Configuration"
cd gh-pages
git remote rm origin
git remote add origin https://$GITHUB_USER:$GITHUB_TOKEN@github.com/$GITHUB_USER/async-gamequery-lib.git

REPO=`git config remote.origin.url`

echo "Remote origin updated to : $REPO"

# Save some useful information
# REPO=`git config remote.origin.url`
# SSH_REPO=${REPO/https:\/\/github.com\//git@github.com:}
# SHA=`git rev-parse --verify HEAD`

# Clean gh-pages
echo "Cleaning gh-pages branch"
git rm -rf .
git clean -fxd

ls -l

# Copy the contents from the site staging directory
if [ ! -d "../target/staging" ]; then
    echo "Site staging directory does not exist"
    exit 1
fi

# Start copying (at this point we should still be inside gh-pages)
cp -vR ../target/staging/ `pwd`

# Git Add
git add .

git config --global user.email "ribasco@gmail.com"
git config --global user.name "AGQL Travis CI"
git config --global push.default simple

# Commit to the branch
git commit -m "Travis CI Site Update for Job #$TRAVIS_JOB_NUMBER"

# Push to the remote repository
git push https://$GITHUB_TOKEN@github.com/ribasco/async-gamequery-lib

ls -la

echo "Site has been deployed!"