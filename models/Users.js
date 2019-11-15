const mongoose = require('mongoose');

const UserSchema = mongoose.Schema({

    firstName:{
        type:String,
        required:true
    },
    lastName:{
        type:String,
        required:true
    },
    email:{
        type:String,
        required:true,
        unique:true
    },
    password:{
        type:String,
        required:true
    },
    gender:{
        type:String,
        required:true
    },
    city:{
        type:String,
        required:true
    },
    dob:{
        type:Date,
        default:Date.now  
    },
})

module.exports=mongoose.model('user',UserSchema)