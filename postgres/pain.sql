DROP VIEW IF EXISTS eventData;
CREATE VIEW eventDATA AS
SELECT a.cip, e.event_id, e.start_time, e.end_time, transport_name AS requestedTransport
FROM event e
JOIN attendance a ON a.event_id = e.event_id
JOIN studentdata sd ON sd.cip = a.cip;


DROP VIEW IF EXISTS requiredRoutes;
CREATE VIEW requiredRoutes AS
select * from (	
select true as arrival, *
from eventdata
union all
select false as arrival, *
from eventdata	
) as a
order by event_id, cip, arrival desc;

DROP VIEW IF EXISTS routeDetails;s
CREATE VIEW routeDetails AS
select rr.cip, arrival, rr.event_id, requestedtransport,
case when arrival = false then place_id else home_place_Id end as startPlace,
case when arrival = true then place_id else home_place_id end as targetPlace,
case when arrival = true then start_time else null end as timeToArrive,
case when arrival = false then end_time else null end timeToLeave
from requiredRoutes rr, studentdata sd, localizedevent le
where rr.cip = sd.cip and le.event_id = rr.event_id;


DROP VIEW IF EXISTS trajectInfo;
CREATE VIEW trajectInfo AS
select distinct cip, arrival, event_id, requestedtransport as transport, startplace, targetplace, timetoarrive, timetoleave, begin_time, end_time
from routedetails rd
left join traject t on requestedtransport = transport_name;


DROP VIEW IF EXISTS usedTrajects;
CREATE VIEW usedTrajects AS
select cip, arrival, event_id, transport, startplace, targetplace, timetoarrive, timetoleave,
case when transport='TRANSIT' then begin_time else null end as begin_time,
case when transport='TRANSIT' then end_time else null end as end_time
from trajectinfo;


DROP VIEW IF EXISTS MissingTraject;
CREATE VIEW MissingTraject AS
select cip, arrival, event_id, requestedtransport, startplace, targetplace, timetoarrive, timetoleave, begin_time, end_time
from routedetails rd
left join traject t on
t.transport_name = rd.requestedtransport and startplace = start_place_id and targetplace = end_place_id and
((rd.requestedtransport = 'TRANSIT' and ((arrival = true and timetoarrive::time = end_time) or (arrival = false and timetoleave::time = begin_time))) or rd.requestedtransport <> 'TRANSIT')
where (start_place_id is null or end_place_id is null) and (timetoarrive > current_date or timetoleave > current_date)
order by timetoarrive desc, timetoleave desc;


DROP VIEW IF EXISTS UserTrajectEvent;
CREATE VIEW UserTrajectEvent AS
select cip, arrival, event_id, requestedtransport, startplace, targetplace, timetoarrive, timetoleave, begin_time, end_time
from routedetails rd
join traject t on
t.transport_name = rd.requestedtransport and startplace = start_place_id and targetplace = end_place_id and
((rd.requestedtransport = 'TRANSIT' and ((arrival = true and timetoarrive::time = end_time) or (arrival = false and timetoleave::time = begin_time))) or rd.requestedtransport <> 'TRANSIT')
order by timetoarrive desc, timetoleave desc;

