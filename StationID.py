import requests
import json

def get_station_details(station_id):
    url = "https://www.gasbuddy.com/graphql"
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
    }
    payload = {
        "operationName": "GetStation",
        "variables": {
            "id": station_id
        },
        "query": "query GetStation($id: ID!) { station(id: $id) { prices { credit { nickname postedTime price } } } }"
    }

    # Sending the POST request
    response = requests.post(url, headers=headers, data=json.dumps(payload))
    
    # Checking if the request was successful
    if response.status_code == 200:
        data = response.json()
        
        print()
        print(data)
        print()
        try:
            # Extracting gas price and additional attributes
            price = data["data"]["station"]["prices"][0]["credit"]["price"]
            nickname = data["data"]["station"]["prices"][0]["credit"]["nickname"]
            postedTime = data["data"]["station"]["prices"][0]["credit"]["postedTime"]

            # Print the results
            print(f"Station ID {station_id} - Standard Gas Price: ${price}")
            print(f"Nickname: {nickname}, Posted Time: {postedTime}")
        except (IndexError, KeyError):
            print("Data structure is different than expected or no data available.")
    elif response.status_code == 403:
        print("Access Forbidden: 403 Error - Check if an API key is required or try changing headers.")
    else:
        print(f"Error {response.status_code}: Could not retrieve data.")

# Replace "STATION_ID_HERE" with the actual station ID
get_station_details("22078")
