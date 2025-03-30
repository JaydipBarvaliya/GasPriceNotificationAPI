import requests
import json

def get_gas_stations(zip_code):
    url = "https://www.gasbuddy.com/graphql"
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "Mozilla/5.0"
    }
    payload = {
        "operationName": "LocationBySearchTerm",
        "variables": {
            "search": zip_code,
            "fuel": 1,  # Optional: Specify the fuel type
            "maxAge": 0  # Optional: Specify maximum age of data
        },
        "query": """
            query LocationBySearchTerm($search: String, $fuel: Int, $maxAge: Int) {
                locationBySearchTerm(search: $search) {
                    countryCode
                    displayName
                    latitude
                    longitude
                    regionCode
                    stations(fuel: $fuel, maxAge: $maxAge) {
                        count
                        results {
                            id
                            name
                            address {
                                line1
                                locality
                                postalCode
                                region
                            }
                            prices {
                                cash {
                                    price
                                    formattedPrice
                                }
                                credit {
                                    price
                                    formattedPrice
                                }
                            }
                            distance
                            starRating
                            ratingsCount
                        }
                    }
                }
            }
        """
    }

    # Sending the POST request
    response = requests.post(url, headers=headers, data=json.dumps(payload))
    
    if response.status_code == 200:
        data = response.json()
        
        print()
        print()
        print()
        # print(data)
        print()
        print()
        print()
        stations = data.get("data", {}).get("locationBySearchTerm", {}).get("stations", {}).get("results", [])
        print(stations)
        print()
        print()
        print()
        
        # for station in stations:
        #     print(f"Station Name: {station['name']}")
        #     print(f"Address: {station['address']['line1']}, {station['address']['locality']}, {station['address']['postalCode']}, {station['address']['region']}")
        #     print(f"Cash Price: ${station['prices']['cash']['price']}")
        #     print(f"Credit Price: ${station['prices']['credit']['price']}")
        #     print(f"Distance: {station['distance']} miles")
        #     print(f"Rating: {station['starRating']} ({station['ratingsCount']} ratings)")
        #     print("-------------------------------------------------")
    else:
        print(f"Error {response.status_code}: Could not retrieve data.")

# Replace with the actual ZIP code you want to search for
get_gas_stations("L5V1M3")
