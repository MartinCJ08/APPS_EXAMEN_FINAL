var express = require('express');
var router = express.Router();
var Task=require('../models/Task');
 
router.get('/:id?',function(req,res,next){
     if(req.params.id){ 
        Task.getTaskById(req.params.id,function(err,rows){
            if(err)
            {
                res.json(err);
            }
            else{
                res.json(rows);
            }
        });
    }
    else{
        Task.getAllTasks(function(err,rows){
            if(err)
            {
                res.json(err);
            }
            else
            {
                res.json(rows);
            }
    
        });
    }
 });

 router.get('/fechas/:d/:m/:y',function(req,res){
     const d = req.params.d;
     const m = req.params.m;
     const y = req.params.y;

    var strDate = d.concat('/',m,'/',y);
    
    // res.json({
    //     day: d,
    //     month: m,
    //     year: y,
    //     date: strDate
    // });

    Task.getTaskByDate(strDate, function(err,rows){
        if(err)
        {
            res.json(err);
        }
        else{
            res.json(rows);
        }
    }); 
 });
 
 router.post('/',function(req,res,next){
    Task.addTask(req.body,function(err,count){
        if(err)
        {
            res.json(err);
        }
        else{
            res.json(req.body);//or return count for 1 &amp;amp;amp; 0
        }
    });
 });
 
 router.delete('/:id',function(req,res,next){
    Task.deleteTask(req.params.id,function(err,count){
        if(err)
        {
            res.json(err);
        }
        else
        {
            res.json(count);
        } 
    });
});

router.put('/:id',function(req,res,next){ 
    Task.updateTask(req.params.id,req.body,function(err,rows){
        if(err)
        {
            res.json(err);
        }
        else
        {
            res.json(rows);
        }
    });
 });
 module.exports=router;