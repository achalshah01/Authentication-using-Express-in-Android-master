const jwt = require("jsonwebtoken");
const config=require('config');

module.exports= (req,res,next)=>{
const token=req.header("x-auth-token")

if(!token){
    return res.status(401).json({msg:"No token ,authorization denied"})
}
else{
    jwt.verify(token,config.get('jewSecret'),(err,decoded)=>{
        if(!decoded){
            return res.status(401).json({msg:"invalied token ,authorization denied"}) 
        }
        console.log("request ="+req.user);
        req.user=decoded.user;
        next();
        
    })
}
}