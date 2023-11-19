drop database if exists sql12663094; -- smartstudyhub
create database sql12663094; -- smartstudyhub

use sql12663094;

drop table if exists users;
drop table if exists report;
drop table if exists vote;
drop table if exists study_group;
drop table if exists manage_users;
drop table if exists add_friend_users;
drop table if exists users_join_study_group;
drop table if exists theme;
drop table if exists folder;
drop table if exists project;
drop table if exists works;
drop table if exists extra_work;
drop table if exists tag;
drop table if exists work_tag;
drop table if exists sound_concentration;
drop table if exists category_forum;
drop table if exists post_forum;
drop table if exists like_post;
drop table if exists comment_post;
drop table if exists report_post;
drop table if exists like_comment;

create table users(
	id int auto_increment,
    user_name varchar(50),
    email varchar(50), 
    passwords text,
    phone_number varchar(11),
    first_name nvarchar(50),
    last_name nvarchar(50),
    address nvarchar(100),
    date_of_birth date,
    country nvarchar(50),
    created_at datetime,
    roles varchar(20) not null,
    image_url text,
    otp_code text,
    otp_time_expiration datetime,
    total_time_focus int,
    provider text,
    status text, -- It have 2 value: ACTIVE, BANNED and DELETED
    constraint UserPrimaryKey primary key(id)
);

create table manage_users(
	user_manager_id int not null,
    user_id int not null,
    created_date datetime,
    constraint ManageUserPrimaryKey primary key(user_manager_id, user_id),
	constraint ManageUsersUserManagerIdForeignKey foreign key(user_manager_id) references users(id),
	constraint ManageUsersUserIdForeignKey foreign key(user_id) references users(id)
);

create table friend_users(
	user_id int not null,
    user_id_friend int not null,
    created_date datetime,
    status text, -- BLOCK, FRIEND, UNFRIEND
    constraint FriendUserPrimaryKey primary key(user_id, user_id_friend),
    constraint FriendUsersUserIdForeignKey foreign key(user_id) references users(id),
	constraint FriendUsersUserIdFriendForeignKey foreign key(user_id_friend) references users(id)
);

create table report(
	id int auto_increment,
    user_id int not null,
    email varchar(50), 
    phonenumber varchar(11),
    title nvarchar(100),
    content nvarchar(300),
    status_report text,
    created_date datetime,
    type_report nvarchar(50),
    status text,
	constraint ReportPrimaryKey primary key(id),
    constraint ReportUserIdForeignKey foreign key(user_id) references users(id)
);

create table vote(
	id int auto_increment,
    user_id int,
    title nvarchar(100),
    content nvarchar(300),
    created_date datetime,
    number_star int,
    status text,
	constraint VotePrimaryKey primary key(id),
    constraint VoteUserIdForeignKey foreign key(user_id) references users(id)
);

create table study_group(
	id int auto_increment,
    codes text,
    name_group nvarchar(50),
    descriptions nvarchar(300),
    total_member int,
    total_time_focus int,
    image_url text,
    created_date datetime,
    status_group text, -- It have 2 value: PRIVATE, PUBLIC
    status text,
	constraint StudyGroupPrimaryKey primary key(id)
);

create table users_join_study_group(
	user_id int not null,
    study_group_id int not null,
    created_date datetime,
    status text, -- JOINED, UNJOIN, BANNED
    constraint UserJoinStudyGroupPrimaryKey primary key(user_id, study_group_id),
    constraint UserJoinStudyGroupUserIdForeignKey foreign key(user_id) references users(id),
    constraint UserJoinStudyGroupStudyGroupIdForeignKey foreign key(study_group_id) references study_group(id)
);

create table theme(
	id int auto_increment,
    user_id int,
    name_sound nvarchar(50),
    url text,
    status_theme text, -- It have 2 value: DEFAULT, OWNED
    status text,
    constraint ThemePrimaryKey primary key(id),
    constraint ThemeUserIdForeignKey foreign key(user_id) references users(id)
);

create table folder(
	id int auto_increment,
    user_id int not null,
    folder_name nvarchar(50),
    color_code text,
    icon_url text,
    status text,
    constraint FolderPrimaryKey primary key(id),
    constraint FolderUserIdForeignKey foreign key(user_id) references users(id)
);

create table project(
	id int auto_increment,
	user_id int not null,
    folder_id int,
    project_name nvarchar(50),
    color_code text,
    icon_url text,
    status text,
    constraint ProjectPrimaryKey primary key(id),
    constraint ProjectFolderId foreign key(folder_id) references folder(id),
    constraint ProjectUserIdForeignKey foreign key(user_id) references users(id)
);

create table works(
	id int auto_increment,
    user_id int not null,
    project_id int,
    status_work text,
    due_date datetime,
    work_name nvarchar(100),
    priority nvarchar(20),
    number_of_pomodoros int,
    time_of_pomodoro int,
    start_time datetime,
    end_time datetime,
    is_remindered boolean,
    is_repeated boolean,
    note nvarchar(300),
    assignee_id int,
    status text,
    constraint WorksPrimaryKey primary key(id),
    constraint WorksUserIdForeignKey foreign key(user_id) references users(id),
    constraint WorksProjectIdForeignKey foreign key(project_id) references project(id),
    constraint WorksAssigneeIdForeignKey foreign key(assignee_id) references users(id)
);

create table extra_work(
	id int auto_increment,
    work_id int not null,
    status nvarchar(20),
    start_time datetime,
    end_time datetime,
    constraint ExtraWorkPrimaryKey primary key(id),
    constraint ExtraWorkWorkIdForeignKey foreign key(work_id) references works(id)
);

create table tag(
	id int auto_increment,
    user_id int not null,
    tag_name nvarchar(50),
    color_code text,
    status text,
    constraint TagPrimaryKey primary key(id),
    constraint TagUserIdForeignKey foreign key(user_id) references users(id)
);

create table work_tag(
	work_id int not null,
    tag_id int not null,
    constraint WorkTagPrimaryKey primary key(work_id, tag_id),
    constraint WorkTagWorkIdForeignKey foreign key(work_id) references works(id),
    constraint WorkTagTagIdForeignKey foreign key(tag_id) references tag(id)
);

create table sound_concentration(
	id int auto_increment,
    user_id int,
    name_sound nvarchar(50),
    url text,
    status_sound text, -- It have 3 value: DEFAULT, PREMIUM, OWNED
    status text,
    constraint SoundConcentrationPrimaryKey primary key(id),
    constraint SoundConcentrationUserIdForeignKey foreign key(user_id) references users(id)
);

create table category_forum(
	id int auto_increment,
    name_category nvarchar(50),
    total_post int,
    total_comment int,
    constraint CategoryForumPrimaryKey primary key(id)
);

create table post_forum(
	id int auto_increment,
    user_id int not null,
    category_forum_id int not null,
    title nvarchar(50),
    content nvarchar(3000),
    tag text,
    image_url text,
    total_like int,
    total_view int,
    created_date datetime,
    url_post text,
    total_type_react text,
    limits text,
    status_post text, -- It have 2 value: ACTIVE, DELETED, BANNED
    status text,
    constraint PostForumPrimaryKey primary key(id),
    constraint PostForumUserIdForeignKey foreign key(user_id) references users(id),
    constraint PostForumCategoryForumIdForeignKey foreign key(category_forum_id) references category_forum(id)
);

create table like_post(
	post_id int not null,
    user_id int not null,
    created_date datetime,
    type_react text,
    constraint LikePostPrimaryKey primary key(post_id, user_id),
    constraint LikePostPostIdForeignKey foreign key(post_id) references post_forum(id),
    constraint LikePostUserIdForeignKey foreign key(user_id) references users(id)
);

create table comment_post(
	id int auto_increment,
	post_id int not null,
    user_id int not null,
    content nvarchar(3000),
    created_date datetime,
    total_like int,
    total_type_react text,
    status text,
    constraint CommentPostPrimaryKey primary key(id),
	constraint CommentPostPostIdForeignKey foreign key(post_id) references post_forum(id),
    constraint CommentPostUserIdForeignKey foreign key(user_id) references users(id)
);

create table like_comment(
	comment_id int not null,
    user_id int not null,
    created_date datetime,
    type_react text,
    constraint LikeCommentPrimaryKey primary key(comment_id, user_id),
    constraint LikeCommentCommentIdForeignKey foreign key(comment_id) references comment_post(id),
    constraint LikeCommentUserIdForeignKey foreign key(user_id) references users(id)
);

create table report_post(
	id int auto_increment,
	user_id int not null,
    post_id int not null,
    content nvarchar(3000),
    created_date datetime,
    status_report text,
    status text,
    constraint ReportPostPrimaryKey primary key(id),
    constraint ReportPostUserIdForeignKey foreign key(user_id) references users(id),
    constraint ReportPostPostIdForeignKey foreign key(post_id) references post_forum(id)
);



