agt=navigator.userAgent.toLowerCase();
isIE=((agt.indexOf("msie")!=-1)&&(agt.indexOf("opera")==-1));
isOpera=(agt.indexOf("opera")!=-1);
isMac=(agt.indexOf("mac")!=-1);
isKHTML=(agt.toLowerCase().indexOf("khtml")!=-1);
isMacIE=(isIE&&isMac);
isWinIE=(isIE&&!isMac);
isGecko=(navigator.product=="Gecko");
var target;
ddDiv=null;
ddList=null;
IFrame=null;
req=null;
wait=null;
theURL=null;
TAB=9;
DELETE=46;
BACKSPACE=8;
LEFT_ARROW=37;
RIGHT_ARROW=39;
HOME=36;
END=35;
PAGE_UP=33;
PAGE_DOWN=34;
UP_ARROW=38;
DOWN_ARROW=40;
ESC=27;
ENTER=13;
SPACE=32;
NBSP=160;
COMMA=44;
SEMI_COLON=59;
SHIFT_KEY=16;
CTRL_KEY=17;
ALT_KEY=18;
LEFT_MS_WINDOWS_KEY=91;
RIGHT_MS_WINDOWS_KEY=92;
MS_MENU_KEY=93;
function getElementY(element)
{
	var targetTop=0;
	var mheight=element.clientHeight;
	if(element.offsetParent)
	{
		while((element.offsetParent&&isIE&&element.currentStyle.styleFloat!="left")||(element.offsetParent&&!isIE))
		{
			if(element.currentStyle!=null)
			{
				if(element.currentStyle.position!='absolute')
					targetTop+=element.offsetTop
			}
			else
			{
				var mposition=document.defaultView.getComputedStyle(element,'position').getPropertyValue('position');
				if(mposition!=null&&mposition!='absolute')
					targetTop+=element.offsetTop
			}
			element=element.offsetParent
		}
	}
	else if(element.y)
	{
		targetTop+=element.y
	}
	return targetTop+mheight+2
}
function getElementX(element)
{
	var targetLeft=0;
	if(element.offsetParent)
	{
		while((element.offsetParent&&isIE&&element.currentStyle.styleFloat!="left")||(element.offsetParent&&!isIE))
		{
			if(element.currentStyle!=null)
			{
				if(element.currentStyle.position!='absolute')
					targetLeft+=element.offsetLeft
			}
			else
			{
				var mposition=document.defaultView.getComputedStyle(element,'position').getPropertyValue('position');
				if(mposition!=null&&mposition!='absolute')
					targetLeft+=element.offsetLeft}element=element.offsetParent
			}
		}
	else if(element.x)
	{
		targetLeft+=element.x
	}
	return targetLeft
}
function initRequest()
{
	try
	{
		req=new ActiveXObject("Microsoft.XMLHTTP")
	}
	catch(e)
	{
		try
		{
			req=new ActiveXObject("Msxml2.XMLHTTP")
		}
		catch(sc)
		{
			req=null
		}
	}
	if(!req&&typeof XMLHttpRequest!="undefined")
	{
		req=new XMLHttpRequest()
	}
	return req
}
stopEvent=function(event)
{
	event.returnValue=false;
	if(event.preventDefault)
	{
		event.preventDefault();
	}
};
function initTarget(field)
{
	target=field;
	var init=target.getAttribute('init');
	if(!init)
	{
		var mOnblur=target.onblur;
		target.onkeypress=function(event)
			{
				FOnKeyPress(event);
			};
		target.onkeydown=function(event)
			{
				FOnKeyDown(event);
			};
		target.onblur=function()
			{
				if(typeof mOnblur=='function')
				{
					mOnblur()
				}
				hideList();
			};
		target.setAttribute('init','ok');
	}
}
function doCompletion(event,field,name,property,value,label,decoration,url)
{
	if(event==null)event=window.event;
	var keyCode=event.keyCode;
	switch(keyCode)
		{
			case HOME:
			case END:
			case PAGE_UP:
			case PAGE_DOWN:
			case UP_ARROW:
			case DOWN_ARROW:
			case RIGHT_ARROW:
			case ESC:
				if(isActive())
					stopEvent(event);
				return;
			case ENTER:
				return;
			default:
				break
		}
	initTarget(field);
	target.setAttribute("decoration",decoration);
	if(target.value=='')
	{
		hideList();
	}
	else
	{
		if(url!=null)
			url+="&ch="+escape(target.value);
		else
		{
			url="easyComplete.do?ch="+escape(target.value)+"&name="+name;
			if(property)
				url+="&property="+property;
			if(value)
				url+="&value="+value;
			if(label)
				url+="&label="+label
		}
		if(req&&req.readyState!=0)
		{
			req.abort()
		}
		if(wait!=null)
		{
			clearTimeout(wait);
		}
		theURL=url;
		wait=setTimeout("sendRequest()",500);
	}
}
function sendRequest()
{
	if(true)
	{
		target.style.backgroundImage="url('theme/charte_v02_002/img/picto/lesMetiers/arTr_UR.gif')"
	}
	req=initRequest();
	req.onreadystatechange=processRequest;
	req.open("GET",theURL,true);
	req.send(null);
	wait=null
}
function showList()
{
	ddDiv.style.display='block';
	ddDiv.style.height=ddList.offsetHeight;
	if(!isGecko&&!isOpera)
	{
		var size=ddList.offsetWidth;
		if(size<target.offsetWidth)
			size=target.offsetWidth;
		ddDiv.style.width=size;
		ddList.style.width=size
	}
	if(IFrame)
	{
		IFrame.style.left=ddDiv.style.left;
		IFrame.style.top=ddDiv.style.top;
		IFrame.width=ddList.offsetWidth;
		IFrame.height=ddList.offsetHeight;
		IFrame.style.zIndex=2;
		ddDiv.style.zIndex=IFrame.style.zIndex+1;
		IFrame.style.display='block'
	}
	if(isGecko||isOpera)
	{
		var size=getOffsetWidthGecko(ddList);
		if(isOpera)
		{
			size+=5
		}
		if(size<target.offsetWidth)
			size=target.offsetWidth-5;
		ddList.style.width=size;
		ddDiv.style.width=size;
	}
	function getOffsetWidthGecko(element)
	{
		var divs=element.childNodes;
		var i;
		var size=0;
		for(i=0;i<divs.length;i++)
		{
			if(divs[i].childNodes[0].offsetWidth>size)
			{
				size=divs[i].childNodes[0].offsetWidth
			}
		}
		return size
	}
	if(document.body.clientWidth<(getElementX(target)+ddDiv.clientWidth))
	{
		var cst=0;
		if(isGecko)
			cst=5;
		ddDiv.style.left=document.body.clientWidth-ddDiv.clientWidth-cst;
		if(IFrame)
		{
			IFrame.style.left=ddDiv.style.left
		}
	}
	ddList.setAttribute("selectedIndex",0);
	target.title=''
}
function isActive()
{
	return ddDiv!=null&&ddDiv.style.display!='none'
}
function hideList()
{
	if(ddDiv!=null)
		ddDiv.style.display='none';
	if(IFrame!=null)
		IFrame.style.display='none';
	if(ddList!=null)ddList.setAttribute("selectedIndex",0);
}
function processRequest()
{
	if(req!=null)
	{
		if(req.readyState==4)
		{
			if(req.status==200)
			{
				createPopup();
				parseMessages();
				if(true)
				{
					target.style.backgroundImage="url('theme/charte_v02_002/img/picto/infoCompagnie/arTr_UR.gif')"
				}
			}
			else if(req.status==204)
			{
				clearList();
				hideList();
			}
			else
			{
				hideList();
			}
		}
	}
}
function createPopup()
{
	if(ddDiv==null)
	{
		ddDiv=document.createElement("div");
		ddDiv.style.position="absolute";
		ddDiv.style.zIndex=1;
		ddDiv.style.display='none';
		if(isIE)
		{
			IFrame=document.createElement("iframe");
			IFrame.style.position="absolute";
			IFrame.frameborder='no';
			IFrame.scrolling="no";
			IFrame.src="javascript:false"	
		}
		ddList=document.createElement("div");
		ddList.className="ddlist";
		ddList.id="ac_select";
		ddList.setAttribute("selectedIndex",0);
		ddList.unselectable="on";
		if(IFrame)
		{
			target.parentNode.insertBefore(IFrame,target.nextSibling);
		}
		target.parentNode.insertBefore(ddDiv,target.nextSibling);
		ddDiv.appendChild(ddList);
	}
	ddDiv.style.top=getElementY(target)+"px";
	ddDiv.style.left=getElementX(target)+"px"
}
function updateList(op)
{
	var selectedIndex=ddList.getAttribute("selectedIndex");
	if(selectedIndex==null)
	{
		selectedIndex=0
	}
	var newSelection=selectedIndex;
	switch(op)
	{
		case"selectPrev":
			newSelection--;
			break;
		case"selectNext":
			newSelection++;
			break;
		default:
			break
	}
	if(newSelection<0)
	{
		newSelection=0
	}
	if(newSelection>(ddList.childNodes.length-1))
	{
		newSelection=(ddList.childNodes.length-1);
	}
	if(newSelection!=selectedIndex)
	{
		select(ddList.childNodes[newSelection]);
	}
}
function parseMessages()
{
	clearList();
	var collection=req.responseXML.getElementsByTagName("collection")[0];
	if(collection==null||collection.childNodes.length==0)
	{
		hideList();
	}
	else
	{
		for(loop=0;loop<collection.childNodes.length&&loop<10;loop++)
		{
			var item=collection.childNodes[loop];
			var value=item.getElementsByTagName("value")[0];
			var label=item.getElementsByTagName("label")[0];
			var mhidden=item.getElementsByTagName("hidden")[0];
			var mlabelvalue=null;
			if(label)
			{
				mlabelvalue=label.childNodes[0].nodeValue
			}
			var mhiddenvalue=null;
			if(mhidden)
			{
				mhiddenvalue=mhidden.childNodes[0].nodeValue
			}
			appendItem(value.childNodes[0].nodeValue,mlabelvalue,mhiddenvalue,loop);
		}
		showList();
	}
}
function clearList(){
	if(ddList!=null)
	{
		for(loop=ddList.childNodes.length-1;loop>=0;loop--)
		{
			ddList.removeChild(ddList.childNodes[loop]);
		}
		ddDiv.style.width=0;ddList.style.width=0
	}
}
function FOnKeyDown(event)
{
	if(event==null)
		event=window.event;
	if(event==null)
		return;
	var keyCode=event.keyCode;
	if(event.ctrlKey||event.ctrlLeft||event.altKey||event.altLeft||event.metaKey)
	{
		switch(keyCode)
		{
			case UP_ARROW:
			case DOWN_ARROW:
				break
		}
	return
	}
	var op=null;
	switch(keyCode)
	{
		case ESC:
			if(isActive())
			{
				hideList();
				stopEvent(event);
			}
			break;
		case UP_ARROW:
			op="selectPrev";
			break;
		case DOWN_ARROW:
			if(isActive)
			{
				op="selectNext"
			}
			break;
		case TAB:
			if(isActive())
			{
				write();
				if(!isIE)
				{
					stopEvent(event);
				}
			}
			break;
		case RIGHT_ARROW:
			break;
		case ENTER:
		{
			if(isActive()&&isWinIE)
			{
				write();
				stopEvent(event);
			}
		}
		default:
			break
	}
	if(op!=null)
	{
		updateList(op);
		stopEvent(event);
	}
}
function FOnKeyPress(event)
{
	if(event==null)
		event=window.event;
	var keyCode=event.keyCode;
	if(keyCode==0)
	{
		keyCode=event.charCode
	}
	if(keyCode==0)
	{
		keyCode=event.which
	}
	if(event.charCode!=null&&event.charCode==0)
	{
		switch(keyCode)
		{
			case ESC:
			case HOME:
			case END:
			case PAGE_UP:
			case PAGE_DOWN:
			case UP_ARROW:
			case DOWN_ARROW:
			case RIGHT_ARROW:
				if(!event.ctrlKey&&!event.altKey&&!event.metaKey&&isActive())
				{
					stopEvent(event);
				}
				return
			}
	}
	switch(keyCode)
	{
		case ENTER:
			if(isActive()&&isGecko)
			{
				write();
				stopEvent(event);
				return
			}
		case TAB:
			if(isActive())
			{
				write();
				stopEvent(event);
			}
			break;
		default:
			break
	}
}
function select(element)
{
	var selid=ddList.getAttribute("selectedIndex");
	ddList.childNodes[selid].className='notsel';
	element.className='sel';
	ddList.setAttribute("selectedIndex",element.getAttribute("index"));
}
function write()
{
	var selid=ddList.getAttribute("selectedIndex");
	target.value=ddList.childNodes[selid].getAttribute("ivalue");
	target.title=ddList.childNodes[selid].getAttribute("label");
	target.setAttribute("hiddenvalue",ddList.childNodes[selid].getAttribute("hidden"));
	hideList();
	eval(target.onitemselection);
}
function appendItem(value,label,hidden,index)
{
	var option=document.createElement("div");
	var deco=target.getAttribute("decoration");
	option.className="notsel";
	if(index==0)
		option.className="sel";
	option.setAttribute("index",index);
	option.setAttribute("ivalue",value);
	option.setAttribute("label",label);
	option.setAttribute("hidden",hidden);
	option.onmousedown=write;
	option.onmouseover=function(event)
	{
		select(this);
	};
	if(ddList!=null)
		ddList.appendChild(option);
	var mrexexp=new RegExp('\\b('+target.value+')','gi');
	if(label)
		option.innerHTML="<nobr>"+value.replace(mrexexp,"<span class="+deco+">$1</span>")+" - <i>"+label.replace(mrexexp,"<span class='"+deco+"'>$1</span>")+"</i></nobr>";
	else 
		option.innerHTML="<nobr>"+value.replace(mrexexp,"<span class="+deco+">$1</span>")+"</nobr>"
}