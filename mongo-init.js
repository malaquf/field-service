db.createUser(
        {
            user: "malaquf",
            pwd: "myPsw",
            roles: [
                {
                    role: "readWrite",
                    db: "app"
                }
            ]
        }
);