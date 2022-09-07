INSERT INTO role (pk_roles_id, name) VALUES (1, 'User');
INSERT INTO role (pk_roles_id, name) VALUES (2, 'Admin');

INSERT INTO public.users(
	pk_users_id, enabled, password, provider, username)
VALUES (2, true, 'Password123', 'form', 'test@gmail.com');


INSERT INTO public.users_roles(
	pk_users_roles, role_id, user_id)
VALUES (2, 2, 2);