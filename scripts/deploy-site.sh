#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

SOURCE_BRANCH="master"
TARGET_BRANCH="gh-pages"
GITHUB_USER="ribasco"

# Do not deploy on pull-requests or commits on other branches
if [ "$TRAVIS_PULL_REQUEST" != "false" -o "$TRAVIS_BRANCH" != "$SOURCE_BRANCH" ]; then
    echo "Skipping deploy. Performing site-build only."
    scripts/build-site.sh
    exit 0
fi

# Start Building the Site
scripts/build-site.sh

# Clone Github Pages Branch
echo "Extracting latest gh-pages from remote origin"
git clone -b $TARGET_BRANCH --single-branch https://$GITHUB_USER:$GITUB_TOKEN@github.com/$GITHUB_USER/async-gamequery-lib.git $TARGET_BRANCH

# Update Remote Configuration
cd gh-pages
# git remote rm origin
# git remote add origin https://$GITHUB_USER:$GITHUB_TOKEN@github.com/$GITHUB_USER/async-gamequery-lib.git

REMOTE_ORIGIN=`git config remote.origin.url`

# Clean gh-pages
echo ===========================================
echo "Cleaning gh-pages branch"
echo ===========================================
git rm -rf .
git clean -fxd

ls -l

# Copy the contents from the site staging directory
if [ ! -d "../target/staging" ]; then
    echo "Site staging directory does not exist"
    exit 1
fi

echo ===========================================
echo "Copying site staging files to `pwd`"
echo ===========================================
# Start copying (at this point we should still be inside gh-pages)
cp -R ../target/staging/* `pwd`

echo ===========================================
echo "Adding files to Git"
echo ===========================================

# Git Add
git add .

echo ===========================================
echo "Configuring commit author details"
echo ===========================================

git config --global user.email "raffy@ibasco.com"
git config --global user.name "AGQL Travis CI"
git config --global push.default simple

echo ===========================================
echo "Committing branch"
echo ===========================================
git commit -m "Travis CI Site Update for Job #$TRAVIS_JOB_NUMBER"

echo ===========================================
echo "Pushing updates to remote repository"
echo ===========================================

# Push to the remote repository
git push --quiet https://${GITHUB_TOKEN}@github.com/ribasco/async-gamequery-lib

ls -la

echo "Site has been deployed!"