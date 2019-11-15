const mongoose = require('mongoose');const Items = mongoose.Schema({

    userId:{
        type:String,
        required:true
    },
    image:{
        type:String,
        required:true
    },
    name:{
        type:String,
        required:true,
        unique:true
    },
    price:{
        type:String,
        required:true
    },
    count:{
        type:Number,
       
    },
   
})

module.exports=mongoose.model('item',Items)