var db=require('../dbconnection'); //reference of dbconnection.js
 
var Task={ 
    getAllTasks:function(callback){ 
        return db.query("Select * from weather",callback);
    },
    getTaskById:function(id,callback){ 
        return db.query("select * from weather where id=?",[id],callback);
    },
    addTask:function(Task,callback){
        return db.query("INSERT INTO weather (temp, hum, lat, lon, fecha, hour) VALUES (?,?,?,?,?,?)",
        [Task.temp, Task.hum, Task.lat,Task.lon,Task.fecha, Task.hour], callback);
        // return db.query("insert into products(productname,unitprice) values(?,?)",[Task.Productname,Task.Unitprice],callback);
    },
    deleteTask:function(id,callback){
        return db.query("delete from weather where id=?",[id],callback);
    },
    updateTask:function(id,Task,callback){
        return db.query("update products set Productname=?, Unitprice=? where productid=?",[Task.Productname,Task.Unitprice,id],callback);
    }  
};

module.exports=Task;