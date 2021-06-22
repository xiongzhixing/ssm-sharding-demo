Array.prototype.pushNoRepeat = function(){
    for(var i=0; i<arguments.length; i++){
      var ele = arguments[i];
      if(this.indexOf(ele) == -1){
          this.push(ele);
      }
  }
};


var elements = [];
var a =document.getElementsByTagName("span").length;
for(var i=0;i<a;i++){
	try{
		var b = document.getElementsByTagName("span")[i];
	    if(b.getAttribute('class').indexOf('itemTitle') <= -1){
			continue;
		}

		if(b.innerText){
			//console.log(b.innerText)
			elements.pushNoRepeat(b.innerText);
		}
	}catch{
	}
}


var a =document.getElementsByTagName("span").length;
for(var i=0;i<a;i++){
	try{
		var b = document.getElementsByTagName("span")[i];
	    if(b.getAttribute('class').indexOf('title') <= -1){
			continue;
		}

		if(b.innerText){
			//console.log(b.innerText)
			elements.pushNoRepeat(b.innerText);
		}
	}catch{
	}
}


var a =document.getElementsByTagName("div").length;
for(var i=0;i<a;i++){
	try{
		var b = document.getElementsByTagName("div")[i];
	    if(b.getAttribute('class').indexOf('title') <= -1){
			continue;
		}

		if(b.innerText){
			//console.log(b.innerText)
			elements.pushNoRepeat(b.innerText);
		}
	}catch{
	}
}

var a =document.getElementsByTagName("p").length;
for(var i=0;i<a;i++){
	try{
		var b = document.getElementsByTagName("p")[i];
	    if(b.getAttribute('class').indexOf('title') <= -1){
			continue;
		}

		if(b.innerText){
			//console.log(b.innerText)
			elements.pushNoRepeat(b.innerText);
		}
	}catch{
	}
}

var a =document.getElementsByClassName("RankIcon").length;
for(var i=0;i<a;i++){
	try{
		var b = document.getElementsByClassName("RankIcon")[i].nextElementSibling;
		if(b.innerText){
			//console.log(b.innerText)
			elements.pushNoRepeat(b.innerText);
		}
	}catch{
	}
}

console.log(elements);