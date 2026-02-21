#!/bin/bash

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
UNLEASH_URL="http://localhost:4242"
TOKEN="*:*.unleash-default-token"

# Function to create a feature flag
create_flag() {
    local name=$1
    local description=$2

    echo -e "${BLUE}Creating flag: ${YELLOW}$name${NC}"

    response=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$UNLEASH_URL/api/admin/projects/default/features" \
        -H "Authorization: $TOKEN" \
        -H "Content-Type: application/json" \
        -d "{
            \"name\": \"$name\",
            \"description\": \"$description\",
            \"type\": \"release\"
        }")

    if [ "$response" -eq 201 ] || [ "$response" -eq 200 ]; then
        echo -e "${GREEN}  ✓ Flag '$name' created successfully${NC}"
        return 0
    else
        echo -e "${RED}  ✗ Failed to create flag '$name' (HTTP $response)${NC}"
        return 1
    fi
}

# Function to enable a feature flag
enable_flag() {
    local name=$1

    echo -e "${BLUE}Enabling flag: ${YELLOW}$name${NC}"

    response=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
        "$UNLEASH_URL/api/admin/projects/default/features/$name/environments/development/on" \
        -H "Authorization: $TOKEN")

    if [ "$response" -eq 200 ] || [ "$response" -eq 202 ]; then
        echo -e "${GREEN}  ✓ Flag '$name' enabled${NC}"
        return 0
    else
        echo -e "${RED}  ✗ Failed to enable flag '$name' (HTTP $response)${NC}"
        return 1
    fi
}

# Function to check if Unleash is ready
check_unleash() {
    echo -e "${BLUE}Checking if Unleash is ready...${NC}"

    for i in {1..30}; do
        response=$(curl -s -o /dev/null -w "%{http_code}" "$UNLEASH_URL/health")
        if [ "$response" -eq 200 ]; then
            echo -e "${GREEN}✓ Unleash is ready!${NC}"
            return 0
        fi
        echo -e "${YELLOW}Waiting for Unleash... ($i/30)${NC}"
        sleep 2
    done

    echo -e "${RED}✗ Unleash failed to start${NC}"
    return 1
}

# Function to verify flags exist
verify_flags() {
    echo -e "\n${BLUE}Verifying flags:${NC}"

    flags=("premium-pricing" "order-notifications" "bulk-order-discount")

    for flag in "${flags[@]}"; do
        response=$(curl -s -o /dev/null -w "%{http_code}" \
            "$UNLEASH_URL/api/admin/projects/default/features/$flag" \
            -H "Authorization: $TOKEN")

        if [ "$response" -eq 200 ]; then
            echo -e "${GREEN}  ✓ $flag exists${NC}"
        else
            echo -e "${RED}  ✗ $flag missing${NC}"
        fi
    done
}

# Main execution
main() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}  Feature Flag Initialization   ${NC}"
    echo -e "${BLUE}================================${NC}\n"

    # Check if Unleash is ready
    check_unleash || exit 1

    echo -e "\n${BLUE}Step 1: Creating flags${NC}"
    echo -e "${BLUE}------------------------${NC}"

    # Create all three flags
    create_flag "premium-pricing" "When enabled, applies a 10% discount to all product prices for premium users"
    create_flag "order-notifications" "When enabled, logs order confirmation notifications (simulating email/SMS)"
    create_flag "bulk-order-discount" "When enabled, applies a 15% discount when order quantity exceeds 5 items"

    echo -e "\n${BLUE}Step 2: Enabling flags${NC}"
    echo -e "${BLUE}------------------------${NC}"

    # Enable all flags
    enable_flag "premium-pricing"
    enable_flag "order-notifications"
    enable_flag "bulk-order-discount"

    # Verify all flags
    verify_flags

    echo -e "\n${GREEN}================================${NC}"
    echo -e "${GREEN}  Initialization Complete!      ${NC}"
    echo -e "${GREEN}================================${NC}"
    echo -e "\n${BLUE}Access Unleash UI: ${YELLOW}http://localhost:4242${NC}"
    echo -e "${BLUE}Login: ${YELLOW}admin / unleash4all${NC}"
}

# Run the main function
main