create table oauth_client_details (
    client_id VARCHAR(256) PRIMARY KEY,
    resource_ids VARCHAR(256),
    client_secret VARCHAR(256),
    scope VARCHAR(256),
    authorized_grant_types VARCHAR(256),
    web_server_redirect_uri VARCHAR(256),
    authorities VARCHAR(256),
    access_token_validity INTEGER,
    refresh_token_validity INTEGER,
    additional_information VARCHAR(4096),
    autoapprove VARCHAR(256)
);

insert into oauth_client_details(
client_id
, client_secret
, resource_ids
, scope
, authorized_grant_types
, web_server_redirect_uri
, authorities
, access_token_validity
, refresh_token_validity
, additional_information
, autoapprove
) values (
'auth_id' -- client id
, '{noop}auth_secret' -- client secret
, null
, 'read,write' -- scope
, 'authorization_code,password,client_credentials,implicit,refresh_token' -- 인증 타입
, null
, 'ROLE_MY_CLIENT' -- 권한
, 36000 -- access_token 유효시간
, 2592000  -- refresh_token 유효시간
, null
, null);