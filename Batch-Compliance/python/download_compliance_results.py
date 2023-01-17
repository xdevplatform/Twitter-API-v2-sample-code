import requests

# Replace with your job download_url
download_url = ''


def connect_to_endpoint(url):
    response = requests.request("GET", url)
    print(response.status_code)
    if response.status_code != 200:
        raise Exception(response.status_code, response.text)
    return response.text


def main():
    response = connect_to_endpoint(download_url)
    entries = response.splitlines()
    for entry in entries:
        print(entry)


if __name__ == "__main__":
    main()
