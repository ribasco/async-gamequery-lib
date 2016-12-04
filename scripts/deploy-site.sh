#!/bin/bash
set -ev # Exit with nonzero exit code if anything fails

TARGET_BRANCH="gh-pages"
GITHUB_USER="ribasco"
GITHUB_AUTHOR_NAME="AGQL Travis CI"
GITHUB_AUTHOR_EMAIL="raffy@ibasco.com"
SITE_STAGE_DIRECTORY="../target/staging"

# Do not deploy site on pull-requests or commits on other branches
if [ "${TRAVIS_PULL_REQUEST}" != "false" -o "${TRAVIS_BRANCH}" != "master" ]; then
    echo "Skipping deploy. Performing site-build only."
    scripts/build-site.sh
    exit 0
fi

# Start Building the Site
scripts/build-site.sh

# Clone Github Pages Branch
echo "Extracting latest gh-pages from remote origin"
git clone -b ${TARGET_BRANCH} --single-branch https://${GITHUB_USER}:${GITUB_TOKEN}@github.com/${GITHUB_USER}/async-gamequery-lib.git ${TARGET_BRANCH}

# Update Remote Configuration
cd ${TARGET_BRANCH}
echo "Switched to directory $(pwd)"

# Clean gh-pages
echo ===========================================
echo "Cleaning ${TARGET_BRANCH} branch"
echo ===========================================
git rm -rf .
git clean -fxd

ls -l

# Copy the contents from the site staging directory
if [ ! -d ${SITE_STAGE_DIRECTORY} ]; then
    echo "Site staging directory does not exist"
    exit 1
fi

echo ===========================================
echo "Copying site staging files to $(pwd)"
echo ===========================================
# Start copying (at this point we should still be inside gh-pages)
cp -vR ../target/staging/* $(pwd)

echo ===========================================
echo "Adding files to Git"
echo ===========================================

# Add all files for versioning
git add .

echo ===========================================
echo "Configuring commit author details"
echo ===========================================

echo "Using GitHub Details = ${GITHUB_AUTHOR_EMAIL} and ${GITHUB_AUTHOR_NAME}"

git config --global user.email "raffy@ibasco.com"
git config --global user.name "Travis CI"
git config --global push.default simple

echo ===========================================
echo "Committing branch"
echo ===========================================
git commit -m "Travis CI Site Update for Job #${TRAVIS_JOB_NUMBER}"

echo ===========================================
echo "Pushing updates to remote repository"
echo ===========================================

# Push to the remote repository
git push --quiet https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/ribasco/async-gamequery-lib

if [ $? -eq 0 ]; then
    echo "Site updates have been successfully committed to the '${TARGET_BRANCH}' branch"
else
    echo "Failed to commit site to remote repository"
fi

ls -la

echo "Site has been deployed!"