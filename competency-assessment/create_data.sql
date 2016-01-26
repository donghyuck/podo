
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (50,'CODESET',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (51,'CODESET_VALUE',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (53,'COMPETENCY',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (54,'ESSENTIAL_ELEMENT',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (55,'PERFORMANCE_CRITERIA',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (56,'ABILITY',1);

Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (60,'JOB',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (65,'RATING_SCHEME',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (66,'RATING_LEVEL',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (70,'ASSESSMENT',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (71,'ASSESSMENT_SCHEME',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (72,'ASSESSMENT_JOB_SELECTION',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (73,'ASSESSMENT_SUBJECT',1);


// data setting 

delete ca_codeset ;

delete ca_job;

delete ca_competency ;

delete ca_essential_element ;

delete CA_ESSENTIAL_ELEMENT ;

update V2_SEQUENCER set value = 1 where name = 'JOB' ;

update V2_SEQUENCER set value = 1 where name = 'CODESET' ;

update V2_SEQUENCER set value = 1 where name = 'COMPETENCY' ;

update V2_SEQUENCER set value = 1 where name = 'ESSENTIAL_ELEMENT' ;

// 

	INSERT INTO CA_COMPETENCY (
		COMPETENCY_ID,
		COMPETENCY_TYPE,
		OBJECT_TYPE,
		OBJECT_ID,
		NAME,
		COMPETENCY_LEVEL,		
		COMPETENCY_GROUP_CODE	
	) VALUES ( 8256, 0, 1, 1, '의사소통능력', 0, '01' )	