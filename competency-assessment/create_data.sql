
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (50,'CODESET',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (51,'CODESET_VALUE',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (53,'COMPETENCY',1);
Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (54,'ESSENTIAL_ELEMENT',1);

Insert into V2_SEQUENCER (SEQUENCER_ID,NAME,VALUE) values (60,'JOB',1);



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


