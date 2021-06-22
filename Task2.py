import pandas as pd
import numpy as np 
import matplotlib as plt
import urllib.request
import json
import datetime as dt

endpoint = 'https://maps.googleapis.com/maps/api/directions/json?'
api_key = 'AIzaSyCNhtFmAGfeHwOfBf-E4pzLlvfPTFkXu38'
ref_date = dt.datetime(1970,1,1,00,00,00)

#import uid, olat, olon, dlat, dlon, departure_date, departure_time
InputFileName = input('Input the name of the File: ')
input_file = InputFileName + '.xlsx'
OutputFileName = InputFileName + '-output'
output_file = open(OutputFileName+".csv","w+")
output_file.write("uid,olat,olon,dlat,dlon,dd,mm,yyyy,hh,mm,ss,traffic_model,distance,duration_under_freeflow,seg1_mode,seg1_distance,seg1_duration,seg2_mode,seg2_distance,seg2_duration,seg2_mode,seg2_distance,seg2_duration,seg3_mode,seg3_distance,seg3_duration,seg4_mode,seg4_distance,seg4_duration,seg5_mode,seg5_distance,seg5_duration,seg6_mode,seg6_distance,seg6_duration,seg7_mode,seg7_distance,seg7_duration,seg8_mode,seg8_distance,seg8_duration,seg9_mode,seg9_distance,seg9_duration,"+"\n")

dataIneed = pd.read_excel(input_file)
uid_array = dataIneed['uid']
olat_array = dataIneed['olat']
olon_array = dataIneed['olon']
dlat_array = dataIneed['dlat']
dlon_array = dataIneed['dlon']
dd_array = dataIneed['dd']
mm_array = dataIneed['mm']
yyyy_array = dataIneed['yyyy']
hh_array = dataIneed['hh']
mi_array = dataIneed['mi']
ss_array = dataIneed['ss']
traffic_model_array = dataIneed['traffic_model']

#Enter The Size of the Batch
b = 12
#Start the For Loop 
for i in range(b):
    if (dd_array[i] == 0):
        departure_time = 'now'
    else:
        s_format = dt.datetime(yyyy_array[i],mm_array[i],dd_array[i],hh_array[i],mi_array[i],ss_array[i])
        departure_time = int((s_format - ref_date).total_seconds())
    traffic_model = traffic_model_array[i]
    nav_request = 'origin={},{}&destination={},{}&departure_time={}&mode=transit&traffic_model={}&key={}'.format(olat_array[i],olon_array[i],dlat_array[i],dlon_array[i],departure_time,traffic_model,api_key)
    #'origin=Delhi&destination=Kolkata&key={}'.format(api_key)
    request = endpoint + nav_request
    response = urllib.request.urlopen(request).read()
    directions = json.loads(response)
    routes = directions['routes']
    #print(directions)
    element1 = routes[0]
    legs = element1['legs']
    elementsIneed = legs[0]
    distance = elementsIneed['distance']
    #print (distance['text'])
    duration = elementsIneed['duration']
    rsteps = elementsIneed['steps']
    output_file.write(str(uid_array[i])+","+str(olat_array[i])+","+str(olon_array[i])+","+str(dlat_array[i])+","+ str(dlon_array[i])+","+str(dd_array[i])+','+str(mm_array[i])+','+str(yyyy_array[i])+','+str(hh_array[i])+','+str(mi_array[i])+','+str(ss_array[i])+","+traffic_model_array[i]+','+distance['text']+","+duration['text']+",")
    for j in range(len(rsteps)):
        transit = rsteps[j]
        if (transit['travel_mode'] == 'WALKING'):
            distace1 = transit['distance']
            duration1 = transit ['duration']
            output_file.write('Walking' + ','+ distace1['text'] + ',' + duration1['text'] + ',')

        else:
            transit_details = transit['transit_details']
            line = transit_details['line']
            vehicle = line['vehicle']
            vehicle1 = vehicle['type']
            distace1 = transit['distance']
            duration1 = transit ['duration']
            output_file.write(vehicle1 + ','+ distace1['text'] + ',' + duration1['text'] + ',')
    output_file.write('\n')


output_file.close()