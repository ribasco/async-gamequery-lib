#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

SOURCE_BRANCH="master"
TARGET_BRANCH="gh-pages"

# Pull requests and commits to other branches shouldn't try to deploy, just build to verify
if [ "$TRAVIS_PULL_REQUEST" != "false" -o "$TRAVIS_BRANCH" != "$SOURCE_BRANCH" ]; then
    echo "Skipping deploy; just doing a build."
    ./build-site.sh
    exit 0
fi

# Save some useful information
REPO=`git config remote.origin.url`
SSH_REPO=${REPO/https:\/\/github.com\//git@github.com:}
SHA=`git rev-parse --verify HEAD`

ls -l

echo "Using Repository URL: $SSH_REPO"

# Clone Github Pages Branch
git clone -b gh-pages --single-branch https://ribasco:$GITUB_TOKEN@github.com/ribasco/async-gamequery-lib.git gh-pages

# Re-add the remote configuration
cd gh-pages
git remote rm origin
git remote add origin https://ribasco:$GITHUB_TOKEN@github.com/ribasco/async-gamequery-lib.git

ls -la

cd ..

ls -l

echo "Building Site"

scripts/build-site.sh

echo "Site Build Complete!"

ls -l