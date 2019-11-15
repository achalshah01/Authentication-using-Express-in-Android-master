const express = require("express");
const router = express.Router();
const User = require("../models/Users");
const { check, validationResult } = require("express-validator");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const config=require('config');

router.post(
	"/",
	[
		check("firstName", "First Name is equiredr")
			.not()
			.isEmpty(),
		check("lastName", "Last Name is equiredr")
			.not()
			.isEmpty(),
		check("email", "Email is equiredr").isEmail(),
		check("password", "Enter a password with 6 or more character").isLength({
			min: 6
		}),
		check("gender", "Gender is equiredr")
			.not()
			.isEmpty(),
		check("city", "City is equiredr")
			.not()
			.isEmpty()
	],
	(req, res) => {
		const errors = validationResult(req);
		if (!errors.isEmpty()) {
			return res.status(400).json({ errors: errors.array() });
		}
		const { firstName, lastName, gender, city, email, password } = req.body;

		User.findOne({ email }, (err, post) => {
			if (err) {
				return err;
			}
			if (post) {
				return res.status(400).json({ msg: "User already exists" });
			} else {
				user = new User({
					firstName,
					lastName,
					email,
					password,
					gender,
					city
				});
				bcrypt.genSalt(10, (err, salt) => {
					bcrypt.hash(password, salt, (err, hash) => {
						user.password = hash;

						user.save(err => {
							//return res.send("user saves");
							const payload={
								user:{
									id:user.id
								}
							}
							jwt.sign(payload,config.get('jewSecret'),{
								expiresIn:360000
							},(error,token)=>{
								if(error) throw err;
								res.json({token});
							})
						});

						//return res.send(err);
					});
				});
			}
		});
	}
);

module.exports = router;
