const express = require("express");
const router = express.Router();
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const config = require("config");
const User = require("../models/Users");
const { check, validationResult } = require("express-validator");
const auth = require("../middleware/auth");



router.get("/", auth, (req, res) => {
	if (res) {
		User.findById(req.user.id, (err, post) => {
			console.log("POST===" + post);
			return res.json(post);
		}).select("-password");
	} else {
		return res.status(400).json(err);
	}
});

router.post(
	"/",
	[
		check("email", "Please include a valid email").isEmail(),
		check("password", "password is requires")
			.not()
			.isEmpty()
	],
	(req, res) => {
		const errors = validationResult(req);
		if (!errors.isEmpty()) {
			return res.status(400).json({ errors: errors.array() });
		}

		const { email, password } = req.body;
		User.findOne({ email }, (error, item) => {
			if (!item) {
				return res.status(400).json({ msg: "User credentials invalid" });
			} else {
				console.log("password==" + password);
				console.log("item.password==" + item.password);
				bcrypt.compare(password, item.password, (err, response) => {
					console.log("response==" + res);

					if (!res) {
						console.log("error==" + err);
						return res.status(400).json({ msg: "User credentials invalid" });
					} else {
						const payload = {
							user: {
								id: item.id
							}
						};
						jwt.sign(
							payload,
							config.get("jewSecret"),
							{
								expiresIn: 360000
							},
							(error, token) => {
								if (error) throw err;
								if (token) {
									console.log("itwm===" + token);
									console.log("response===" + res);

									return res.send({ token });
								}
							}
						);
					}
				});
			}
		});
	}
);

module.exports = router;
