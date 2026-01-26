import requests
import json

API_KEY = "AIzaSyD00MFn5Q_xxRTGdjIIAUV1UcNGBaZgeao"

# Test locations
LOCATIONS = [
    ("Brampton, Ontario, Canada", 43.7315, -79.7624),  # Canada test
    ("Detroit, Michigan, USA", 42.3314, -83.0458)      # US test
]

SEARCH_RADIUS_METERS = 3000  # 3 km

def search_nearby_gas_stations(lat, lng):
    url = "https://places.googleapis.com/v1/places:searchNearby"
    headers = {
        "Content-Type": "application/json",
        "X-Goog-Api-Key": API_KEY,
        "X-Goog-FieldMask": "places.id,places.displayName,places.formattedAddress"
    }
    payload = {
        "locationRestriction": {
            "circle": {
                "center": {"latitude": lat, "longitude": lng},
                "radius": SEARCH_RADIUS_METERS
            }
        },
        "includedTypes": ["gas_station"],
        "maxResultCount": 5
    }
    response = requests.post(url, headers=headers, json=payload)
    if response.status_code != 200:
        print("❌ Error fetching nearby gas stations:", response.text)
        return []
    return response.json().get("places", [])

def get_fuel_prices(place_id):
    url = f"https://places.googleapis.com/v1/places/{place_id}?fields=fuelOptions"
    headers = {
        "Content-Type": "application/json",
        "X-Goog-Api-Key": API_KEY
    }
    response = requests.get(url, headers=headers)
    if response.status_code != 200:
        print(f"❌ Error fetching fuel prices for {place_id}:", response.text)
        return None, None
    try:
        json_data = response.json()
    except Exception:
        print("⚠️ Could not parse JSON for:", place_id)
        return None, None
    return json_data.get("fuelOptions", []), json_data

if __name__ == "__main__":
    for location_name, lat, lng in LOCATIONS:
        print("\n" + "="*60)
        print(f"🔍 Testing fuel prices for: {location_name}")
        print("="*60)

        stations = search_nearby_gas_stations(lat, lng)

        if not stations:
            print("⚠️ No gas stations found.")
            continue

        for station in stations:
            name = station.get("displayName", {}).get("text", "Unknown")
            address = station.get("formattedAddress", "Unknown address")
            place_id = station.get("id")

            print(f"\n⛽ {name}")
            print(f"📍 {address}")

            fuel_prices, raw_json = get_fuel_prices(place_id)

            if fuel_prices:
                for fuel in fuel_prices:
                    if isinstance(fuel, dict):
                        fuel_type = fuel.get("type", "Unknown")
                        price_info = fuel.get("price", {})
                        units = price_info.get("units", "0")
                        nanos = price_info.get("nanos", 0)
                        currency = price_info.get("currencyCode", "")
                        try:
                            price = float(units) + nanos / 1e9
                            print(f"   {fuel_type}: {price:.3f} {currency}")
                        except Exception:
                            print(f"   {fuel_type}: Price format issue")
                    else:
                        print(f"   Unknown fuel data format: {fuel}")
            else:
                print("⚠️ No structured fuel price data found.")
                print("📄 Full API Response:")
                print(json.dumps(raw_json, indent=2))
