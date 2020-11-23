# -*- coding: utf-8 -*-
import requests

__author__ = "Waterball (johnny850807@gmail.com)"
__license__ = "Apache 2.0"

TAIPEI_STATION = {"latitude": 25.047713, "longitude": 121.5174007}
DISTANCE_800M = {"latitude": 25.03606, "longitude": 121.50653798}
DISTANCE_300M = {"latitude": 25.04036, "longitude": 121.50787338}
VALENTINES_DAY = 'ValentinesDay'
SPORT = 'Sport'
BUSINESS = 'Business'
NORMAL = 'Normal'
type_translation = {SPORT: '跑車', BUSINESS: '商務', NORMAL: '小轎車'}

passenger = None

USER_SVC = 'http://localhost:8080'
MATCH_SVC = 'http://localhost:8081'
TRIP_SVC = 'http://localhost:8082'
PRICING_SVC = 'http://localhost:8083'


class Struct:
    def __init__(self, **entries):
        self.__dict__.update(entries)


def test():
    global passenger
    passenger = sign_up_as_passenger('Johnny', 'johnny@passenger.com', 'password')
    update_passenger_location(passenger, TAIPEI_STATION, '台北火車站')

    driver_A = sign_up_as_driver('A', 'A@driver.com', 'password', SPORT)
    driver_B = sign_up_as_driver('B', 'B@driver.com', 'password', BUSINESS)
    driver_C = sign_up_as_driver('C', 'C@driver.com', 'password', SPORT)

    update_driver_location(driver_A, DISTANCE_800M, '離台北火車站800公尺')
    update_driver_location(driver_B, DISTANCE_300M, '離台北火車站300公尺')
    update_driver_location(driver_C, DISTANCE_300M, '離台北火車站300公尺')
    #
    # participate_valentines_day(driver_A, VALENTINES_DAY)
    # participate_valentines_day(driver_C, VALENTINES_DAY)
    #
    # match = start_matching(SPORT, VALENTINES_DAY)
    # match = wait_for_match(match.id)
    #
    # assert match.driver_id == driver_C.id
    #
    # trip = pick_up(match.id)
    # arrive(match.id, trip.id)
    #
    # price = pricing(match.id, trip.id)
    # assert int(price) == 1100


def sign_up_as_passenger(name, email, password):
    print(f"{name} 已註冊為乘客。")
    # [POST] /api/users?type=passenger
    return struct(requests.post(f'{USER_SVC}/api/users', params={'type': 'passenger'},
                                json={'name': name, 'email': email, 'password': password}))


def sign_up_as_driver(name, email, password, car_type):
    print(f"{name} 已註冊為司機 [{type_translation[car_type]}]。")
    # [POST] /api/users?type=driver
    return struct(requests.post(f'{USER_SVC}/api/users', params={'type': 'driver'},
                                json={'name': name, 'email': email, 'password': password,
                                      'carType': car_type}))


def participate_valentines_day(driver):
    print(f"司機{driver.name} 參與了情人節活動。")
    # [POST] /api/activities/${activity_name}/drivers/${driver_id}
    assert_ok(requests.post(f'{MATCH_SVC}/api/activities/valentinesDay/drivers/{driver.id}'))


def update_driver_location(user, location, location_alias):
    print(f"司機{user.name} 位於 {location_alias}。")
    # [PUT] /api/users/${driver_id}/location?latitude=&&longitude=
    assert_ok(requests.put(f'{USER_SVC}/api/users/{user.id}/location', params=location))


def update_passenger_location(user, location, location_alias):
    print(f"乘客{user.name} 位於 {location_alias}。")
    # [PUT] /api/users/${passenger_id}/location?latitude=&&longitude=
    assert_ok(requests.put(f"{USER_SVC}/api/users/{passenger.id}/location", params=location))


def start_matching():
    print(f"乘客{passenger.name} 開始匹配...")
    # [POST] /api/users/${passenger_id}/match
    return struct(requests.post(f"{MATCH_SVC}/api/users/{passenger.id}/match"))


def wait_for_match(match_id):
    # poll for matches
    # [GET] /api/users/${passenger_id}/match/${match_id}

    match = struct(requests.get(f'{MATCH_SVC}/api/users/{passenger.id}/match/{match_id}'))
    if match.completed:
        driver = None
        print(f"乘客{passenger.name} 匹配完成 --> 司機{driver.name}")
        return match
    return wait_for_match(match_id)


def pick_up(match_id):
    print(f"乘客{passenger.name} 已上車，開始旅程...")
    # [POST] /api/users/${passenger_id}/match/${match_id}/trip
    assert_ok(requests.post(f"{TRIP_SVC}/api/users/{passenger.id}/match/{match_id}/trip"))


def arrive(match_id, trip_id):
    print(f"乘客{passenger.name} 已抵達，旅程結束。")
    # [POST] /api/users/${passenger_id}/match/${match_id}/trip/${trip_id}/arrive
    assert_ok(requests.post(f'{TRIP_SVC}/api/users/{passenger.id}/match/{match_id}/trip/{trip_id}/arrive'))


def pricing(match_id, trip_id):
    print(f"旅程計價中...")
    # [GET] /api/users/${passenger_id}/match/${match_id}/trip/${trip_id}/price

    results = struct(requests.get(f'{PRICING_SVC}/api/users/{passenger.id}/match/{match_id}/trip/{trip_id}/price'))
    return results.price


def struct(response):
    assert_ok(response)
    return Struct(**response.json())


def assert_ok(response):
    assert response.status_code == 200


if __name__ == '__main__':
    test()
