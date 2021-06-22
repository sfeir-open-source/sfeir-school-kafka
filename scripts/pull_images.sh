#!/usr/bin/env bash

find steps/*-solution -type d -depth 0 -exec sh -c '(cd "$1" && docker-compose pull)' -- {} \;
