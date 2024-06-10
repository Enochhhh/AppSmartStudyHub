-- use smart_study_hub;
-- use sql12663094;
use defaultdb;

-- Users
insert into users values(1, 'enochphann@gmail.com', 'enochphann@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Son', N'Phan', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null, null, null, null, null, null, null, null);
insert into users values(2, 'namdo@gmail.com', 'namdo@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Nam', N'Do', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'PREMIUM', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null, null, null, null, null, null, '2025-12-05 00:00:00', null);
insert into users values(3, 'chienpham@gmail.com', 'chienpham@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Chien', N'Pham', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null, null, null, null, null, null, null, null);
insert into users values(4, 'thaihung@gmail.com', 'thaihung@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Hung', N'Thai', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null, null, null, null, null, null, null, null);
insert into users values(5, 'baodo@gmail.com', 'baodo@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Bao', N'Do', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null, null, null, null, null, null, null, null);
insert into users values(6, null, null, null,
	null, N'1', N'#GUEST ', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'GUEST', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null, null, null, null, null, null, null, null);
insert into users values(7, null, null, null,
	null, N'2', N'#GUEST ', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'GUEST', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null, null, null, null, null, null, null, null);
insert into users values(8, 'phanhongson234@gmail.com', 'phanhongson234@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Son', N'Phan', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'ADMIN', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null, null, null, null, null, null, null, null);
insert into users values(9, 'nmt2002vn@gmail.com', 'nmt2002vn@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Toan', N'Nguyen', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'ADMIN', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null, null, null, null, null, null, null, null);
    
-- Folder
insert into folder values(1, 1, N'Folder 1 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE');
insert into folder values(2, 2, N'Folder 1 Nam Do', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE');
insert into folder values(3, 1, N'Folder 3 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE');

-- Project
insert into project values(1, 1, 1, N'Project 1 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null, null);
insert into project values(2, 1, 1, N'Project 2 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null, null);
insert into project values(3, 2, 2, N'Project 1 Nam Do', '#000000', null, '2023-11-28 00:00:00','ACTIVE', null, null);
insert into project values(4, 1, 1, N'Project 3 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null, null);
insert into project values(5, 1, 1, N'Project 4 Son Phan', '#000000', null, '2023-11-28 00:00:00', 'ACTIVE', null, null);

-- Files
insert into files values(1, null, 'THEME', '/SmartStudyHub/THEME/DEFAULT/', 'File_ADMIN_1_04032024000000_jpg', 'jpg', 'image', 
	'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/File_ADMIN_1_04032024000000_jpg.jpg', 
    '2024-03-14 00:00:00', 'SmartStudyHub/THEME/DEFAULT/File_ADMIN_1_04032024000000_jpg');
insert into files values(2, null, 'THEME', '/SmartStudyHub/THEME/DEFAULT/', 'File_ADMIN_2_04032024000000_jpg', 'jpg', 'image', 
	'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/File_ADMIN_2_04032024000000_jpg.jpg', 
    '2024-03-14 00:00:00', 'SmartStudyHub/THEME/DEFAULT/File_ADMIN_2_04032024000000_jpg');
insert into files values(3, null, 'THEME', '/SmartStudyHub/THEME/DEFAULT/', 'File_ADMIN_3_04032024000000_jpg', 'jpg', 'image', 
	'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/File_ADMIN_3_04032024000000_jpg.jpg', 
    '2024-03-14 00:00:00', 'SmartStudyHub/THEME/DEFAULT/File_ADMIN_3_04032024000000_jpg');
insert into files values(4, null, 'THEME', '/SmartStudyHub/THEME/DEFAULT/', 'File_ADMIN_4_04032024000000_jpg', 'jpg', 'image', 
	'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/File_ADMIN_4_04032024000000_jpg.jpg', 
    '2024-03-14 00:00:00', 'SmartStudyHub/THEME/DEFAULT/File_ADMIN_4_04032024000000_jpg');
insert into files values(5, null, 'THEME', '/SmartStudyHub/THEME/DEFAULT/', 'File_ADMIN_5_04032024000000_jpg', 'jpg', 'image', 
	'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/File_ADMIN_5_04032024000000_jpg.jpg', 
    '2024-03-14 00:00:00', 'SmartStudyHub/THEME/DEFAULT/File_ADMIN_5_04032024000000_jpg');
insert into files values(6, null, 'THEME', '/SmartStudyHub/THEME/PREMIUM/', 'File_ADMIN_6_04032024000000_jpg', 'jpg', 'image', 
	'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954660/SmartStudyHub/THEME/PREMIUM/File_ADMIN_6_04032024000000_jpg.jpg', 
    '2024-03-14 00:00:00', 'SmartStudyHub/THEME/PREMIUM/File_ADMIN_6_04032024000000_jpg');
insert into files values(7, null, 'THEME', '/SmartStudyHub/THEME/PREMIUM/', 'File_ADMIN_7_04032024000000_jpg', 'jpg', 'image', 
	'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954660/SmartStudyHub/THEME/PREMIUM/File_ADMIN_7_04032024000000_jpg.jpg', 
    '2024-03-14 00:00:00', 'SmartStudyHub/THEME/PREMIUM/File_ADMIN_7_04032024000000_jpg');
insert into files values(8, null, 'THEME', '/SmartStudyHub/THEME/PREMIUM/', 'File_ADMIN_8_04032024000000_jpg', 'jpg', 'image', 
	'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954660/SmartStudyHub/THEME/PREMIUM/File_ADMIN_8_04032024000000_jpg.jpg', 
    '2024-03-14 00:00:00', 'SmartStudyHub/THEME/PREMIUM/File_ADMIN_8_04032024000000_jpg');
insert into files values(9, null, 'THEME', '/SmartStudyHub/THEME/PREMIUM/', 'File_ADMIN_9_04032024000000_jpg', 'jpg', 'image', 
	'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954660/SmartStudyHub/THEME/PREMIUM/File_ADMIN_9_04032024000000_jpg.jpg', 
    '2024-03-14 00:00:00', 'SmartStudyHub/THEME/PREMIUM/File_ADMIN_9_04032024000000_jpg');
insert into files values(10, null, 'THEME', '/SmartStudyHub/THEME/PREMIUM/', 'File_ADMIN_10_04032024000000_jpg', 'jpg', 'image', 
	'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954660/SmartStudyHub/THEME/PREMIUM/File_ADMIN_10_04032024000000_jpg.jpg', 
    '2024-03-14 00:00:00', 'SmartStudyHub/THEME/PREMIUM/File_ADMIN_10_04032024000000_jpg');
insert into files values(11, null, 'SOUNDDONE', '/SmartStudyHub/SOUNDDONE/DEFAULT/', 'File_ADMIN_11_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956713/SmartStudyHub/SOUNDDONE/DEFAULT/File_ADMIN_11_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDDONE/DEFAULT/File_ADMIN_11_04032024000000_mp3');
insert into files values(12, null, 'SOUNDDONE', '/SmartStudyHub/SOUNDDONE/DEFAULT/', 'File_ADMIN_12_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956713/SmartStudyHub/SOUNDDONE/DEFAULT/File_ADMIN_12_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDDONE/DEFAULT/File_ADMIN_12_04032024000000_mp3');
insert into files values(13, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/', 'File_ADMIN_13_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_13_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_13_04032024000000_mp3');
insert into files values(14, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/', 'File_ADMIN_14_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_14_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_14_04032024000000_mp3');
insert into files values(15, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/', 'File_ADMIN_15_04032024_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_15_04032024_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_15_04032024_mp3');
insert into files values(16, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/', 'File_ADMIN_16_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_16_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_16_04032024000000_mp3');
insert into files values(17, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/', 'File_ADMIN_17_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_17_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_17_04032024000000_mp3');
insert into files values(18, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/', 'File_ADMIN_18_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_18_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_18_04032024000000_mp3');
insert into files values(19, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/', 'File_ADMIN_19_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_19_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_19_04032024000000_mp3');
insert into files values(20, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/', 'File_ADMIN_20_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_20_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_20_04032024000000_mp3');
insert into files values(21, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/', 'File_ADMIN_21_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_21_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_21_04032024000000_mp3');
insert into files values(22, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/', 'File_ADMIN_22_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956930/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/File_ADMIN_22_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/File_ADMIN_22_04032024_mp3');
insert into files values(23, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/', 'File_ADMIN_23_04032024_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956930/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/File_ADMIN_23_04032024_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/File_ADMIN_23_04032024000000_mp3');
insert into files values(24, null, 'SOUNDCONCENTRATION', '/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/', 'File_ADMIN_24_04032024000000_mp3', 'mp3', 'video', 
	'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956930/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/File_ADMIN_24_04032024000000_mp3.mp3', 
    '2024-03-14 00:00:00', 'SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/File_ADMIN_24_04032024000000_mp3');

-- Theme
insert into theme values(1, null, N'Sunflower Garden', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/File_ADMIN_1_04032024000000_jpg.jpg',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into theme values(2, null, N'Temple Under The Moon', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/File_ADMIN_2_04032024000000_jpg.jpg',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00'); 
insert into theme values(3, null, N'Valdivian Temperate Forests', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954115/SmartStudyHub/THEME/DEFAULT/File_ADMIN_3_04032024000000_jpg.jpg',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into theme values(4, null, N'Desert', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/File_ADMIN_4_04032024000000_jpg.jpg',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into theme values(5, null, N'Chill With Code', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/File_ADMIN_5_04032024000000_jpg.jpg',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into theme values(6, null, N'Boy Under The Moon', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954660/SmartStudyHub/THEME/PREMIUM/File_ADMIN_6_04032024000000_jpg.jpg',
	'PREMIUM', 'ACTIVE', '2024-03-14 00:00:00');
insert into theme values(7, null, N'Sky Full Of Stars', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954660/SmartStudyHub/THEME/PREMIUM/File_ADMIN_7_04032024000000_jpg.jpg',
	'PREMIUM', 'ACTIVE', '2024-03-14 00:00:00');
insert into theme values(8, null, N'Flower Field At Night', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954661/SmartStudyHub/THEME/PREMIUM/File_ADMIN_8_04032024000000_jpg.jpg',
	'PREMIUM', 'ACTIVE', '2023-11-28 00:00:00');
insert into theme values(9, null, N'The Stream', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954661/SmartStudyHub/THEME/PREMIUM/File_ADMIN_9_04032024000000_jpg.jpg',
	'PREMIUM', 'ACTIVE', '2024-03-14 00:00:00');
insert into theme values(10, null, N'The Street', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954661/SmartStudyHub/THEME/PREMIUM/File_ADMIN_10_04032024000000_jpg.jpg',
	'PREMIUM', 'ACTIVE', '2024-03-14 00:00:00');
    
-- Sound Done
insert into sound_done values(1, null, N'Default Bell', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956713/SmartStudyHub/SOUNDDONE/DEFAULT/File_ADMIN_11_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_done values(2, null, N'Done', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956713/SmartStudyHub/SOUNDDONE/DEFAULT/File_ADMIN_12_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');


-- Sound Concentration
insert into sound_concentration values(1, null, N'The Clock', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_13_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(2, null, N'Country Side', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_14_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(3, null, N'Rain And Lightning', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956912/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_15_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(4, null, N'Frog', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956914/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_16_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(5, null, N'Raining', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956915/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_17_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(6, null, N'Running Water', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956915/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_18_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(7, null, N'Small Raining', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956919/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_19_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(8, null, N'Stream', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956921/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_20_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(9, null, N'Tik Tak', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956922/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/File_ADMIN_21_04032024000000_mp3.mp3',
	'DEFAULT', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(10, null, N'Wave', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956930/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/File_ADMIN_22_04032024000000_mp3.mp3',
	'PREMIUM', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(11, null, N'Wind And Leaf', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956931/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/File_ADMIN_23_04032024000000_mp3.mp3',
	'PREMIUM', 'ACTIVE', '2024-03-14 00:00:00');
insert into sound_concentration values(12, null, N'White Noise', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956933/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/File_ADMIN_24_04032024000000_mp3.mp3',
	'PREMIUM', 'ACTIVE', '2024-03-14 00:00:00');


-- Work
insert into works values(1, 1, 1, '2023-12-05 00:00:00', N'Son Phan Work 1', N'HIGH', 6, 25, 48, 0, '2023-11-28 00:00:00', 
	null, false, null, null, '2023-11-28 00:00:00', null,  null, null, null, 'ACTIVE', null, null, null);
insert into works values(2, 1, 1, '2023-12-05 00:00:00', N'Son Phan Work 2', N'LOW', 6, 25, 59, 0, '2023-11-28 00:00:00', 
	null, false, null, null, '2023-11-28 00:00:00', null,  null, null, null, 'ACTIVE', null, null, null);
insert into works values(3, 1, null, '2023-12-05 00:00:00', N'Son Phan Work 3', N'HIGH', 6, 25, 78, 0, '2023-11-28 00:00:00', 
	null, false, null, null, '2023-11-28 00:00:00', null, null, null, null, 'ACTIVE', null, null, null);
