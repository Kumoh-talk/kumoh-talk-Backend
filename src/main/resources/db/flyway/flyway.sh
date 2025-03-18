#!/bin/bash

# Flyway repair 실행
if flyway repair; then
    echo "Flyway repair succeeded."
else
    echo "Flyway repair failed."
    exit 1
fi

# Flyway migrate 실행
if flyway migrate; then
    echo "Flyway migration succeeded."
else
    echo "Flyway migration failed."
    exit 1
fi

# 컨테이너 종료
echo "Shutting down the Flyway container..."
exit 0