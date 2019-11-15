const express = require("express");
const app = express();
const connectDB = require("./config/db");

connectDB();
app.get("/", (req, res) => {
	res.json("hello");
});

app.use(express.json({ extended: false }));

app.use("/api/users", require("./routes/users"));
app.use("/api/auth", require("./routes/auth"));
app.use("/api/client_token", require("./routes/payment"));
app.use("/api/listJson", require("./routes/listJson"));
//app.use("/api/listJson", require("./routes/listJson"));


const port = process.env.PORT || 5000;
app.listen(port, () => {
	console.log(`server started on ${port}`);
});
