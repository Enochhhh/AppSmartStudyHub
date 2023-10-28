drop database if exists smart_study_hub;
create database smart_study_hub;

use smart_study_hub;

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
	id int,
    email varchar(50), 
    passwords varchar(50),
    phonenumber varchar(11),
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
    status text, -- It have 2 value: ACTIVE, BANNED and DELETED
    constraint UserPrimaryKey primary key(id)
);

create table manage_users(
	user_manager_id int,
    user_id int,
    created_date datetime,
    constraint ManageUserPrimaryKey primary key(user_manager_id, user_id),
	constraint ManageUsersUserManagerIdForeignKey foreign key(user_manager_id) references users(id),
	constraint ManageUsersUserIdForeignKey foreign key(user_id) references users(id)
);

create table add_friend_users(
	user_id int,
    user_id_friend int,
    created_date datetime,
    constraint AddFriendUserPrimaryKey primary key(user_id, user_id_friend),
    constraint AddFriendUsersUserIdForeignKey foreign key(user_id) references users(id),
	constraint AddFriendUsersUserIdFriendForeignKey foreign key(user_id_friend) references users(id)
);

create table report(
	id int,
    user_id int,
    email varchar(50), 
    phonenumber varchar(11),
    title nvarchar(100),
    content nvarchar(300),
    status nvarchar(20),
    created_date datetime,
    type_report nvarchar(50),
	constraint ReportPrimaryKey primary key(id),
    constraint ReportUserIdForeignKey foreign key(user_id) references users(id)
);

create table vote(
	id int,
    user_id int,
    title nvarchar(100),
    content nvarchar(300),
    created_date datetime,
    number_star int,
	constraint VotePrimaryKey primary key(id),
    constraint VoteUserIdForeignKey foreign key(user_id) references users(id)
);

create table study_group(
	id int,
    codes text,
    name_group nvarchar(50),
    descriptions nvarchar(300),
    total_member int,
    total_time_focus int,
    image_url text,
    created_date datetime,
    status text, -- It have 2 value: PRIVATE, PUBLIC
	constraint StudyGroupPrimaryKey primary key(id)
);

create table users_join_study_group(
	user_id int,
    study_group_id int,
    created_date datetime,
    constraint UserJoinStudyGroupPrimaryKey primary key(user_id, study_group_id),
    constraint UserJoinStudyGroupUserIdForeignKey foreign key(user_id) references users(id),
    constraint UserJoinStudyGroupStudyGroupIdForeignKey foreign key(study_group_id) references study_group(id)
);

create table theme(
	id int,
    user_id int,
    name_sound nvarchar(50),
    url text,
    status text, -- It have 2 value: DEFAULT, OWNED
    constraint ThemePrimaryKey primary key(id),
    constraint ThemeUserIdForeignKey foreign key(user_id) references users(id)
);

create table folder(
	id int,
    user_id int,
    folder_name nvarchar(50),
    color_code text,
    icon_url text,
    constraint FolderPrimaryKey primary key(id),
    constraint FolderUserIdForeignKey foreign key(user_id) references users(id)
);

create table project(
	id int,
	user_id int,
    folder_id int,
    project_name nvarchar(50),
    color_code text,
    icon_url text,
    constraint ProjectPrimaryKey primary key(id),
    constraint ProjectFolderId foreign key(folder_id) references folder(id),
    constraint ProjectUserIdForeignKey foreign key(user_id) references users(id)
);

create table works(
	id int,
    user_id int,
    project_id int,
    status nvarchar(20),
    due_date datetime,
    work_name nvarchar(100),
    priority nvarchar(20),
    number_of_pomodoros int,
    time_of_pomodoro int,
    start_time datetime,
    end_time datetime,
    is_reminder boolean,
    is_repeat boolean,
    note nvarchar(300),
    assignee_id int,
    constraint WorksPrimaryKey primary key(id),
    constraint WorksUserIdForeignKey foreign key(user_id) references users(id),
    constraint WorksProjectIdForeignKey foreign key(project_id) references project(id),
    constraint WorksAssigneeIdForeignKey foreign key(assignee_id) references users(id)
);

create table extra_work(
	id int,
    work_id int,
    status nvarchar(20),
    start_time datetime,
    end_time datetime,
    constraint ExtraWorkPrimaryKey primary key(id),
    constraint ExtraWorkWorkIdForeignKey foreign key(work_id) references works(id)
);

create table tag(
	id int,
    user_id int,
    tag_name nvarchar(50),
    color_code text,
    constraint TagPrimaryKey primary key(id),
    constraint TagUserIdForeignKey foreign key(user_id) references users(id)
);

create table work_tag(
	work_id int,
    tag_id int,
    constraint WorkTagPrimaryKey primary key(work_id, tag_id),
    constraint WorkTagWorkIdForeignKey foreign key(work_id) references works(id),
    constraint WorkTagTagIdForeignKey foreign key(tag_id) references tag(id)
);

create table sound_concentration(
	id int,
    user_id int,
    name_sound nvarchar(50),
    url text,
    status text, -- It have 2 value: DEFAULT, OWNED
    constraint SoundConcentrationPrimaryKey primary key(id),
    constraint SoundConcentrationUserIdForeignKey foreign key(user_id) references users(id)
);

create table category_forum(
	id int,
    name_category nvarchar(50),
    total_post int,
    total_comment int,
    constraint CategoryForumPrimaryKey primary key(id)
);

create table post_forum(
	id int,
    user_id int,
    category_forum_id int,
    title nvarchar(50),
    content nvarchar(3000),
    tag text,
    image_url text,
    total_like int,
    total_view int,
    created_date datetime,
    url_post text,
    total_type_react text,
    status text, -- It have 2 value: ACTIVE, DELETED, BANNED
    constraint PostForumPrimaryKey primary key(id),
    constraint PostForumUserIdForeignKey foreign key(user_id) references users(id),
    constraint PostForumCategoryForumIdForeignKey foreign key(category_forum_id) references category_forum(id)
);

create table like_post(
	post_id int,
    user_id int,
    created_date datetime,
    type_react text,
    constraint LikePostPrimaryKey primary key(post_id, user_id),
    constraint LikePostPostIdForeignKey foreign key(post_id) references post_forum(id),
    constraint LikePostUserIdForeignKey foreign key(user_id) references users(id)
);

create table comment_post(
	id int,
	post_id int,
    user_id int,
    content text,
    created_date datetime,
    total_like int,
    total_type_react text,
    constraint CommentPostPrimaryKey primary key(id),
	constraint CommentPostPostIdForeignKey foreign key(post_id) references post_forum(id),
    constraint CommentPostUserIdForeignKey foreign key(user_id) references users(id)
);

create table like_comment(
	comment_id int,
    user_id int,
    created_date datetime,
    type_react text,
    constraint LikeCommentPrimaryKey primary key(comment_id, user_id),
    constraint LikeCommentCommentIdForeignKey foreign key(comment_id) references comment_post(id),
    constraint LikeCommentUserIdForeignKey foreign key(user_id) references users(id)
);

create table report_post(
	user_id int,
    post_id int,
    content text,
    created_date datetime,
    constraint ReportPostPrimaryKey primary key(user_id, post_id),
    constraint ReportPostUserIdForeignKey foreign key(user_id) references users(id),
    constraint ReportPostPostIdForeignKey foreign key(post_id) references post_forum(id)
);



