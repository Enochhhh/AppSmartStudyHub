-- use smart_study_hub;
-- use sql12663094;
use defaultdb;

-- Users
insert into users values(1, 'enochphann@gmail.com', 'enochphann@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Son', N'Phan', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null);
insert into users values(2, 'namdo@gmail.com', 'namdo@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Nam', N'Do', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'PREMIUM', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null);
insert into users values(3, 'chienpham@gmail.com', 'chienpham@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Chien', N'Pham', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null);
insert into users values(4, 'thaihung@gmail.com', 'thaihung@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Hung', N'Thai', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null);
insert into users values(5, 'baodo@gmail.com', 'baodo@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Bao', N'Do', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'CUSTOMER', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null);
insert into users values(6, null, null, null,
	null, N'1', N'#GUEST ', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'GUEST', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null);
insert into users values(7, null, null, null,
	null, N'2', N'#GUEST ', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'GUEST', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null);
insert into users values(8, 'phanhongson234@gmail.com', 'phanhongson234@gmail.com', '$2a$10$TODnUc7c4sAS/9xXVJIBreyaaYSyqXWFGPuZcaSQ4w05UN.a6ZMjm',
	null, N'Son', N'Phan', null, null, 'Viet Nam', '2023-11-28 22:31:05', 'ADMIN', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1701175788/SmartStudyHub/USER/default-avatar_c2ruot.png',
    null, 'local', 'ACTIVE', null, false, null, null, null, null);
    
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

-- Theme
insert into theme values(1, null, N'Sunflower Garden', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/bg_focus_1_ntjiwe.jpg',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into theme values(2, null, N'Temple Under The Moon', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/lake_p8iofx.jpg',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00'); 
insert into theme values(3, null, N'Valdivian Temperate Forests', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954115/SmartStudyHub/THEME/DEFAULT/leaf_j89nqp.jpg',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into theme values(4, null, N'Desert', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/sandhill_ls43la.jpg',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into theme values(5, null, N'Chill With Code', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954116/SmartStudyHub/THEME/DEFAULT/coming_soon_eylsbt.jpg',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into theme values(6, null, N'Boy Under The Moon', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954660/SmartStudyHub/THEME/PREMIUM/moon_h4s0lo.jpg',
	'PREMIUM', 'ACTIVE', '2023-11-28 00:00:00');
insert into theme values(7, null, N'Sky Full Of Stars', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954660/SmartStudyHub/THEME/PREMIUM/sky_labjga.jpg',
	'PREMIUM', 'ACTIVE', '2023-11-28 00:00:00');
insert into theme values(8, null, N'Flower Field At Night', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954661/SmartStudyHub/THEME/PREMIUM/nightandstar_ahwy8d.jpg',
	'PREMIUM', 'ACTIVE', '2023-11-28 00:00:00');
insert into theme values(9, null, N'The Stream', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702954661/SmartStudyHub/THEME/PREMIUM/stream_oqvnps.jpg',
	'PREMIUM', 'ACTIVE', '2023-11-28 00:00:00');
insert into theme values(10, null, N'The Street', 'https://res.cloudinary.com/dnj5purhu/image/upload/v1702955022/SmartStudyHub/THEME/PREMIUM/street_uy5nn8.jpg',
	'PREMIUM', 'ACTIVE', '2023-11-28 00:00:00');
    
-- Sound Done
insert into sound_done values(1, null, N'Default Bell', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956713/SmartStudyHub/SOUNDDONE/DEFAULT/DefaultBell_vh2hg0.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_done values(2, null, N'Done', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956713/SmartStudyHub/SOUNDDONE/DEFAULT/Done_webdtm.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');


-- Sound Concentration
insert into sound_concentration values(1, null, N'The Clock', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/clock_xhwqbu.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(2, null, N'Country Side', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956910/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/countrysite_mvlwvn.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(3, null, N'Rain And Lightning', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956912/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/rainandlightning_ccvg5l.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(4, null, N'Frog', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956914/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/frog_jhftfh.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(5, null, N'Raining', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956915/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/raining_jc6qvt.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(6, null, N'Running Water', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956915/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/runningwater_xze5lx.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(7, null, N'Small Raining', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956919/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/smallraing_zzccn7.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(8, null, N'Stream', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956921/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/Stream_xomgtm.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(9, null, N'Tik Tak', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956922/SmartStudyHub/SOUNDCONCENTRATION/DEFAULT/tiktack_iayjsj.mp3',
	'DEFAULT', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(10, null, N'Wave', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956930/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/waves_ejsexp.mp3',
	'PREMIUM', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(11, null, N'Wind And Leaf', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956931/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/wind_and_leaf_depdev.mp3',
	'PREMIUM', 'ACTIVE', '2023-11-28 00:00:00');
insert into sound_concentration values(12, null, N'White Noise', 'https://res.cloudinary.com/dnj5purhu/video/upload/v1702956933/SmartStudyHub/SOUNDCONCENTRATION/PREMIUM/Whitenoise_appq6h.mp3',
	'PREMIUM', 'ACTIVE', '2023-11-28 00:00:00');


-- Work
insert into works values(1, 1, 1, '2023-12-05 00:00:00', N'Son Phan Work 1', N'HIGH', 6, 25, 48, 0, '2023-11-28 00:00:00', 
	null, false, false, null, null, '2023-11-28 00:00:00', null,  null, null, null, null, 'ACTIVE', null);
insert into works values(2, 1, 1, '2023-12-05 00:00:00', N'Son Phan Work 2', N'LOW', 6, 25, 59, 0, '2023-11-28 00:00:00', 
	null, false, false, null, null, '2023-11-28 00:00:00', null,  null, null, null, null, 'ACTIVE', null);
insert into works values(3, 1, null, '2023-12-05 00:00:00', N'Son Phan Work 3', N'HIGH', 6, 25, 78, 0, '2023-11-28 00:00:00', 
	null, false, false, null, null, '2023-11-28 00:00:00', null, null, null, null, null, 'ACTIVE', null);
