function cancelAll(){try{for(var x=0;x<checkedElements.length;x++){restoreField(checkedElements[x][0],checkedElements[x][2]);};window.close();}catch(e){alert(WI18N["msgSpellCommError"]);};};function suggestionsKeyPress(){if(window.event&&window.event.keyCode==13){suggestionsDoubleClick();return false;};};function suggestionsSingleClick(){var selIndex=document.wSpellCheckerBean.suggestions.selectedIndex;if(selIndex>-1){document.wSpellCheckerBean.replaceWith.value=document.wSpellCheckerBean.suggestions.options[selIndex].text;};};function suggestionsDoubleClick(){var selIndex=document.wSpellCheckerBean.suggestions.selectedIndex;if(selIndex>-1){document.wSpellCheckerBean.replaceWith.value=document.wSpellCheckerBean.suggestions.options[selIndex].text;replaceWord(false);};};function miscKeyPress(){if(window.event&&window.event.keyCode==27){window.close();return false;};};function replaceAllInstances(){addToSkipArray();replaceWord(true);};function ignoreAllInstances(){addToSkipArray();processNextMistake();};function ignoreWord(){var c=document.wSpellCheckerBean.original.value,d=0;if(singleIgnoredArray[c]!=null){d=singleIgnoredArray[c];};d++;singleIgnoredArray[c]=d;processNextMistake();};function addToSkipArray(){skipArray[skipArray.length]=document.wSpellCheckerBean.original.value;};function replaceWord(replaceAll){try{replaceWordInField(document.wSpellCheckerBean.original.value,checkedElements[currentElement][0],document.wSpellCheckerBean.replaceWith.value,replaceAll);processNextMistake();}catch(e){alert(WI18N["msgSpellCommError"]);};};function processNextMistake(){currentEvent++;if(!(currentElement>=checkedElements.length)&&currentEvent>=checkedElements[currentElement][1].length){currentElement++;skipArray=new Array(0);singleIgnoredArray=new Object();currentEvent=0;};if(currentElement>=checkedElements.length){alert(WI18N["msgSpellComplete"]);window.close();return;};if(checkedElements[currentElement][1].length==0){processNextMistake();return;};var f=checkedElements[currentElement][1][currentEvent][0];for(var x=0;x<skipArray.length;x++){if(skipArray[x]==f){processNextMistake();return;};};document.wSpellCheckerBean.original.value=f;document.wSpellCheckerBean.suggestions.options.length=0;for(var sug=0;sug<checkedElements[currentElement][1][currentEvent][2].length;sug++){document.wSpellCheckerBean.suggestions.options[sug]=new Option(checkedElements[currentElement][1][currentEvent][2][sug]);};if(document.wSpellCheckerBean.suggestions.options.length>0){document.wSpellCheckerBean.replaceWith.value=document.wSpellCheckerBean.suggestions.options[0].text;document.wSpellCheckerBean.suggestions.options[0].selected=true;}else{document.wSpellCheckerBean.replaceWith.value=f;};try{selectWordInField(f,checkedElements[currentElement][0])}catch(e){alert(WI18N["msgSpellCommError"]);};};function selectWordInField(c,b){var a=eval('opener.document.'+b);if(a.setSelectionRange){selectWordInFieldNotIE(c,b);}else if(a.createTextRange){selectWordInFieldIE(c,b);}else{alert(WI18N["msgSpellPbBrowser"]);};};function replaceWordInField(c,b,e,replaceAll){var a=eval('opener.document.'+b);if(a.setSelectionRange){replaceWordInFieldNotIE(c,b,e,replaceAll);}else if(a.createTextRange){replaceWordInFieldIE(c,b,e,replaceAll);}else{alert(WI18N["msgSpellPbBrowser"]);};};function selectWordInFieldIE(c,b){var a=eval('opener.document.'+b),textRange=a.createTextRange();findAndSelectIE(textRange,c);};function findAndSelectIE(textRange,c){var d=0;if(singleIgnoredArray[c]!=null){d=singleIgnoredArray[c];};for(var x=0;x<=d;x++){if(x>0)textRange.move('character',1);var found=textRange.findText(c,1,6);if(!found)textRange.findText(c,1,4);};textRange.select();};function replaceWordInFieldIE(c,b,e,replaceAll){var a=eval('opener.document.'+b),textRange=a.createTextRange();findAndSelectIE(textRange,c);textRange.text=e;if(replaceAll){var keepGoing=true;while(keepGoing){keepGoing=textRange.findText(c,1,6);if(keepGoing){textRange.select();textRange.text=e;};};};};function selectWordInFieldNotIE(c,b){var a=eval('opener.document.'+b);findAndSelectNotIE(a,c);};function findAndSelectNotIE(a,c){var d=0;if(singleIgnoredArray[c]!=null){d=singleIgnoredArray[c];};var offSet=0,match=false,re=new RegExp('\\b'+c+'\\b','i'),str=a.value,match=re.exec(str);for(var x=1;x<=d;x++){offSet=offSet+match.index+c.length;str=str.substring(match.index+c.length);match=re.exec(str);};if(match){var left=match.index+offSet,right=left+c.length;a.setSelectionRange(left,right);a.focus();return match;}else{return false;};};function replaceWordInFieldNotIE(c,b,e,replaceAll){var a=eval('opener.document.'+b);findAndSelectNotIE(a,c);var selectionStart=a.selectionStart,selectionEnd=a.selectionEnd;a.value=a.value.substring(0,selectionStart)+e+a.value.substring(selectionEnd);if(replaceAll){var keepGoing=true;while(keepGoing){keepGoing=findAndSelectNotIE(a,c);if(keepGoing){selectionStart=a.selectionStart;selectionEnd=a.selectionEnd;a.value=a.value.substring(0,selectionStart)+e+a.value.substring(selectionEnd);};};};a.setSelectionRange(0,0);};function restoreField(b,originalValue){var a=eval('opener.document.'+b);a.value=originalValue;};