const express = require("express");
const router = express.Router();
const User = require("../models/Users");
const { check, validationResult } = require("express-validator");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const config = require("config");
const auth = require("../middleware/auth");
var braintree = require("braintree");

////initializing braintree
var gateway = braintree.connect({
	environment: braintree.Environment.Sandbox,
	merchantId: "96tqg2dssbc8fs9t",
	publicKey: "phm77bm5kw2rhgg4",
	privateKey: "607a32b893b01d2dcc357f67ef788d60"
});

var token;
////send client token through braintree
router.get("/", auth, (req, res) => {
	if (res) {
		token = req.header("x-auth-token");
		gateway.clientToken.generate({}, function(err, response) {
			res.send({ client_token: response.clientToken });
		});
	} else {
		return res.status(400).json(err);
	}
});

gateway.clientToken.generate({}, function(err, response) {
	var clientToken = token;
});

////recieve payment nonce
router.post(
	"/checkout",
	[
		auth,
		check("amount", "amount is requires")
			.not()
			.isEmpty(),
		check("nonce", "nonce is requires")
			.not()
			.isEmpty()
	],
	(req, res) => {
		const errors = validationResult(req);
		if (!errors.isEmpty()) {
			return res.status(400).json({ errors: errors.array() });
		}

		var nonceFromTheClient = req.body.nonce;
		var { amount } = req.body;

		// Use payment method nonce here
		gateway.transaction.sale(
			{
				amount: amount,
				paymentMethodNonce: nonceFromTheClient,
				options: {
					submitForSettlement: true
				}
			},
			(err, result) => {
				if (result) {
					console.log("got");

					res.send({ msg: result.message });
				} else {
					res.send({ msg: err });
				}
			}
		);
	}
);
module.exports = router;
