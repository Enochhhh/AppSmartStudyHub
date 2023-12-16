-- use smart_study_hub;
-- use sql12663094;
use defaultdb;

-- Users
insert into users values(1, 'sonphan@gmail.com', 'sonphan@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Son', N'Phan', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE');
insert into users values(2, 'namdo@gmail.com', 'namdo@gmail.com', '$10$vh60yy1/sLzBye4pW5iv9.8URWuEVIXPkHRc1ICH9vfGPC2lDjLVu',
	null, N'Nam', N'Do', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE');
insert into users values(3, 'chienpham@gmail.com', 'chienpham@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Chien', N'Pham', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE');
insert into users values(4, 'thaihung@gmail.com', 'thaihung@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Hung', N'Thai', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE');
insert into users values(5, 'baodo@gmail.com', 'baodo@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Bao', N'Do', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE');
insert into users values(6, null, null, null,
	null, N'1', N'#GUEST ', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'GUEST', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE');
insert into users values(7, null, null, null,
	null, N'2', N'#GUEST ', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'GUEST', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE');
    
-- Folder
insert into folder values(1, 1, N'Folder 1 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null);
insert into folder values(2, 2, N'Folder 1 Nam Do', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null);
insert into folder values(3, 1, N'Folder 3 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null);

-- Project
insert into project values(1, 1, 1, N'Project 1 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null);
insert into project values(2, 1, 1, N'Project 2 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null);
insert into project values(3, 2, 2, N'Project 1 Nam Do', '#000000', null, '2023-11-28 00:00:00','ACTIVE', null);
insert into project values(4, 1, 1, N'Project 3 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null);
insert into project values(5, 1, 1, N'Project 4 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null);

-- Work
insert into works values(1, 1, 1, '2023-12-05 00:00:00', N'Son Phan Work 1', N'HIGHT', 6, 25, 48, 0, '2023-11-28 00:00:00', 
	null, false, false, null, null, '2023-11-28 00:00:00', null, null, 'ACTIVE', null);
insert into works values(2, 1, 1, '2023-12-05 00:00:00', N'Son Phan Work 2', N'LOW', 6, 25, 59, 0, '2023-11-28 00:00:00', 
	null, false, false, null, null, '2023-11-28 00:00:00', null, null, 'ACTIVE', null);
insert into works values(3, 1, null, '2023-12-05 00:00:00', N'Son Phan Work 3', N'HIGHT', 6, 25, 78, 0, '2023-11-28 00:00:00', 
	null, false, false, null, null, '2023-11-28 00:00:00', null, null, 'ACTIVE', null);
