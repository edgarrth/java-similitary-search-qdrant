#!/usr/bin/env python3
import json
import sys
import urllib.error
import urllib.request
from pathlib import Path

API_URL = "http://localhost:8080/api/v1/merchant-risk-profiles"
DATASET = Path(__file__).with_name("merchant-risk-profiles.json")


def post_profile(profile: dict) -> None:
    body = json.dumps(profile).encode("utf-8")
    request = urllib.request.Request(
        API_URL,
        data=body,
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    with urllib.request.urlopen(request, timeout=10) as response:
        payload = response.read().decode("utf-8")
        print(f"[{response.status}] indexed {profile['profileCode']}: {payload[:120]}")


def main() -> int:
    profiles = json.loads(DATASET.read_text(encoding="utf-8"))
    print(f"Loading {len(profiles)} merchant risk profiles into the microservice API...")
    for profile in profiles:
        try:
            post_profile(profile)
        except urllib.error.HTTPError as exc:
            print(f"HTTP error indexing {profile.get('profileCode')}: {exc.code} {exc.read().decode('utf-8')}", file=sys.stderr)
            return 1
        except urllib.error.URLError as exc:
            print(f"Could not connect to {API_URL}: {exc}", file=sys.stderr)
            print("Make sure Qdrant and the Spring Boot app are running.", file=sys.stderr)
            return 1
    print("Dataset loaded successfully.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
