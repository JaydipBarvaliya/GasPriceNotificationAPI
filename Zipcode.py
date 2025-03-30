import requests
import json

def get_gasbuddy_data(zip_code):
    url = "https://www.gasbuddy.com/graphql"
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
    }
    payload = {
        "operationName": "LocationBySearchTerm",
        "variables": {
            # "fuel": 1,
            # "maxAge": 0,
            "search": zip_code
        },
        "query": "query LocationBySearchTerm($search: String) { locationBySearchTerm(search: $search) { trends { areaName country today todayLow } } }"
    }

    # Sending the POST request
    response = requests.post(url, headers=headers, data=json.dumps(payload))
    
    # Checking if the request was successful
    if response.status_code == 200:
        data = response.json()
        print()
        print()
        print()
        print(data)
        print()
        print()
        print()
        try:
            # Extracting data for the gas prices
            today_price = data["data"]["locationBySearchTerm"]["trends"][0]["today"]
            today_low_price = data["data"]["locationBySearchTerm"]["trends"][0]["todayLow"]
            # print(f"Today's Price: ${today_price}, Today's Lowest Price: ${today_low_price}")
        except (IndexError, KeyError):
            print("Data structure is different than expected or no data available.")
    else:
        print(f"Error {response.status_code}: Could not retrieve data.")

# Replace "YOUR_ZIP_HERE" with the actual zip code
get_gasbuddy_data("L5V1M3")
