/*
	Copyright (c) 2004-2008, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/book/dojo-book-0-9/introduction/licensing
*/

/*
	This is a compiled version of Dojo, built for deployment and not for
	development. To get an editable version, please visit:

		http://dojotoolkit.org

	for documentation and information on getting the source.
*/

if(!dojo._hasResource["dojo.fx"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.fx"] = true;
dojo.provide("dojo.fx");
dojo.provide("dojo.fx.Toggler");

/*=====
dojo.fx = {
	// summary: Effects library on top of Base animations
};
=====*/

(function(){
	var _baseObj = {
			_fire: function(evt, args){
				if(this[evt]){
					this[evt].apply(this, args||[]);
				}
				return this;
			}
		};

	var _chain = function(animations){
		this._index = -1;
		this._animations = animations||[];
		this._current = this._onAnimateCtx = this._onEndCtx = null;

		this.duration = 0;
		dojo.forEach(this._animations, function(a){
			this.duration += a.duration;
			if(a.delay){ this.duration += a.delay; }
		}, this);
	};
	dojo.extend(_chain, {
		_onAnimate: function(){
			this._fire("onAnimate", arguments);
		},
		_onEnd: function(){
			dojo.disconnect(this._onAnimateCtx);
			dojo.disconnect(this._onEndCtx);
			this._onAnimateCtx = this._onEndCtx = null;
			if(this._index + 1 == this._animations.length){
				this._fire("onEnd");
			}else{
				// switch animations
				this._current = this._animations[++this._index];
				this._onAnimateCtx = dojo.connect(this._current, "onAnimate", this, "_onAnimate");
				this._onEndCtx = dojo.connect(this._current, "onEnd", this, "_onEnd");
				this._current.play(0, true);
			}
		},
		play: function(/*int?*/ delay, /*Boolean?*/ gotoStart){
			if(!this._current){ this._current = this._animations[this._index = 0]; }
			if(!gotoStart && this._current.status() == "playing"){ return this; }
			var beforeBegin = dojo.connect(this._current, "beforeBegin", this, function(){
					this._fire("beforeBegin");
				}),
				onBegin = dojo.connect(this._current, "onBegin", this, function(arg){
					this._fire("onBegin", arguments);
				}),
				onPlay = dojo.connect(this._current, "onPlay", this, function(arg){
					this._fire("onPlay", arguments);
					dojo.disconnect(beforeBegin);
					dojo.disconnect(onBegin);
					dojo.disconnect(onPlay);
				});
			if(this._onAnimateCtx){
				dojo.disconnect(this._onAnimateCtx);
			}
			this._onAnimateCtx = dojo.connect(this._current, "onAnimate", this, "_onAnimate");
			if(this._onEndCtx){
				dojo.disconnect(this._onEndCtx);
			}
			this._onEndCtx = dojo.connect(this._current, "onEnd", this, "_onEnd");
			this._current.play.apply(this._current, arguments);
			return this;
		},
		pause: function(){
			if(this._current){
				var e = dojo.connect(this._current, "onPause", this, function(arg){
						this._fire("onPause", arguments);
						dojo.disconnect(e);
					});
				this._current.pause();
			}
			return this;
		},
		gotoPercent: function(/*Decimal*/percent, /*Boolean?*/ andPlay){
			this.pause();
			var offset = this.duration * percent;
			this._current = null;
			dojo.some(this._animations, function(a){
				if(a.duration <= offset){
					this._current = a;
					return true;
				}
				offset -= a.duration;
				return false;
			});
			if(this._current){
				this._current.gotoPercent(offset / _current.duration, andPlay);
			}
			return this;
		},
		stop: function(/*boolean?*/ gotoEnd){
			if(this._current){
				if(gotoEnd){
					for(; this._index + 1 < this._animations.length; ++this._index){
						this._animations[this._index].stop(true);
					}
					this._current = this._animations[this._index];
				}
				var e = dojo.connect(this._current, "onStop", this, function(arg){
						this._fire("onStop", arguments);
						dojo.disconnect(e);
					});
				this._current.stop();
			}
			return this;
		},
		status: function(){
			return this._current ? this._current.status() : "stopped";
		},
		destroy: function(){
			if(this._onAnimateCtx){ dojo.disconnect(this._onAnimateCtx); }
			if(this._onEndCtx){ dojo.disconnect(this._onEndCtx); }
		}
	});
	dojo.extend(_chain, _baseObj);

	dojo.fx.chain = function(/*dojo._Animation[]*/ animations){
		// summary: Chain a list of dojo._Animation s to run in sequence
		// example:
		//	|	dojo.fx.chain([
		//	|		dojo.fadeIn({ node:node }),
		//	|		dojo.fadeOut({ node:otherNode })
		//	|	]).play();
		//
		return new _chain(animations) // dojo._Animation
	};

	var _combine = function(animations){
		this._animations = animations||[];
		this._connects = [];
		this._finished = 0;

		this.duration = 0;
		dojo.forEach(animations, function(a){
			var duration = a.duration;
			if(a.delay){ duration += a.delay; }
			if(this.duration < duration){ this.duration = duration; }
			this._connects.push(dojo.connect(a, "onEnd", this, "_onEnd"));
		}, this);
		
		this._pseudoAnimation = new dojo._Animation({curve: [0, 1], duration: this.duration});
		dojo.forEach(["beforeBegin", "onBegin", "onPlay", "onAnimate", "onPause", "onStop"], 
			function(evt){
				this._connects.push(dojo.connect(this._pseudoAnimation, evt, dojo.hitch(this, "_fire", evt)));
			},
			this
		);
	};
	dojo.extend(_combine, {
		_doAction: function(action, args){
			dojo.forEach(this._animations, function(a){
				a[action].apply(a, args);
			});
			return this;
		},
		_onEnd: function(){
			if(++this._finished == this._animations.length){
				this._fire("onEnd");
			}
		},
		_call: function(action, args){
			var t = this._pseudoAnimation;
			t[action].apply(t, args);
		},
		play: function(/*int?*/ delay, /*Boolean?*/ gotoStart){
			this._finished = 0;
			this._doAction("play", arguments);
			this._call("play", arguments);
			return this;
		},
		pause: function(){
			this._doAction("pause", arguments);
			this._call("pause", arguments);
			return this;
		},
		gotoPercent: function(/*Decimal*/percent, /*Boolean?*/ andPlay){
			var ms = this.duration * percent;
			dojo.forEach(this._animations, function(a){
				a.gotoPercent(a.duration < ms ? 1 : (ms / a.duration), andPlay);
			});
			this._call("gotoProcent", arguments);
			return this;
		},
		stop: function(/*boolean?*/ gotoEnd){
			this._doAction("stop", arguments);
			this._call("stop", arguments);
			return this;
		},
		status: function(){
			return this._pseudoAnimation.status();
		},
		destroy: function(){
			dojo.forEach(this._connects, dojo.disconnect);
		}
	});
	dojo.extend(_combine, _baseObj);

	dojo.fx.combine = function(/*dojo._Animation[]*/ animations){
		// summary: Combine a list of dojo._Animation s to run in parallel
		// example:
		//	|	dojo.fx.combine([
		//	|		dojo.fadeIn({ node:node }),
		//	|		dojo.fadeOut({ node:otherNode })
		//	|	]).play();
		return new _combine(animations); // dojo._Animation
	};
})();

dojo.declare("dojo.fx.Toggler", null, {
	// summary:
	//		class constructor for an animation toggler. It accepts a packed
	//		set of arguments about what type of animation to use in each
	//		direction, duration, etc.
	//
	// example:
	//	|	var t = new dojo.fx.Toggler({
	//	|		node: "nodeId",
	//	|		showDuration: 500,
	//	|		// hideDuration will default to "200"
	//	|		showFunc: dojo.wipeIn, 
	//	|		// hideFunc will default to "fadeOut"
	//	|	});
	//	|	t.show(100); // delay showing for 100ms
	//	|	// ...time passes...
	//	|	t.hide();

	// FIXME: need a policy for where the toggler should "be" the next
	// time show/hide are called if we're stopped somewhere in the
	// middle.

	constructor: function(args){
		var _t = this;

		dojo.mixin(_t, args);
		_t.node = args.node;
		_t._showArgs = dojo.mixin({}, args);
		_t._showArgs.node = _t.node;
		_t._showArgs.duration = _t.showDuration;
		_t.showAnim = _t.showFunc(_t._showArgs);

		_t._hideArgs = dojo.mixin({}, args);
		_t._hideArgs.node = _t.node;
		_t._hideArgs.duration = _t.hideDuration;
		_t.hideAnim = _t.hideFunc(_t._hideArgs);

		dojo.connect(_t.showAnim, "beforeBegin", dojo.hitch(_t.hideAnim, "stop", true));
		dojo.connect(_t.hideAnim, "beforeBegin", dojo.hitch(_t.showAnim, "stop", true));
	},

	// node: DomNode
	//	the node to toggle
	node: null,

	// showFunc: Function
	//	The function that returns the dojo._Animation to show the node
	showFunc: dojo.fadeIn,

	// hideFunc: Function	
	//	The function that returns the dojo._Animation to hide the node
	hideFunc: dojo.fadeOut,

	// showDuration:
	//	Time in milliseconds to run the show Animation
	showDuration: 200,

	// hideDuration:
	//	Time in milliseconds to run the hide Animation
	hideDuration: 200,

	/*=====
	_showArgs: null,
	_showAnim: null,

	_hideArgs: null,
	_hideAnim: null,

	_isShowing: false,
	_isHiding: false,
	=====*/

	show: function(delay){
		// summary: Toggle the node to showing
		return this.showAnim.play(delay || 0);
	},

	hide: function(delay){
		// summary: Toggle the node to hidden
		return this.hideAnim.play(delay || 0);
	}
});

dojo.fx.wipeIn = function(/*Object*/ args){
	// summary
	//		Returns an animation that will expand the
	//		node defined in 'args' object from it's current height to
	//		it's natural height (with no scrollbar).
	//		Node must have no margin/border/padding.
	args.node = dojo.byId(args.node);
	var node = args.node, s = node.style;

	var anim = dojo.animateProperty(dojo.mixin({
		properties: {
			height: {
				// wrapped in functions so we wait till the last second to query (in case value has changed)
				start: function(){
					// start at current [computed] height, but use 1px rather than 0
					// because 0 causes IE to display the whole panel
					s.overflow="hidden";
					if(s.visibility=="hidden"||s.display=="none"){
						s.height="1px";
						s.display="";
						s.visibility="";
						return 1;
					}else{
						var height = dojo.style(node, "height");
						return Math.max(height, 1);
					}
				},
				end: function(){
					return node.scrollHeight;
				}
			}
		}
	}, args));

	dojo.connect(anim, "onEnd", function(){ 
		s.height = "auto";
	});

	return anim; // dojo._Animation
}

dojo.fx.wipeOut = function(/*Object*/ args){
	// summary
	//		Returns an animation that will shrink node defined in "args"
	//		from it's current height to 1px, and then hide it.
	var node = args.node = dojo.byId(args.node);
	var s = node.style;

	var anim = dojo.animateProperty(dojo.mixin({
		properties: {
			height: {
				end: 1 // 0 causes IE to display the whole panel
			}
		}
	}, args));

	dojo.connect(anim, "beforeBegin", function(){
		s.overflow = "hidden";
		s.display = "";
	});
	dojo.connect(anim, "onEnd", function(){
		s.height = "auto";
		s.display = "none";
	});

	return anim; // dojo._Animation
}

dojo.fx.slideTo = function(/*Object?*/ args){
	// summary
	//		Returns an animation that will slide "node" 
	//		defined in args Object from its current position to
	//		the position defined by (args.left, args.top).
	// example:
	//	|	dojo.fx.slideTo({ node: node, left:"40", top:"50", unit:"px" }).play()

	var node = (args.node = dojo.byId(args.node));
	
	var top = null;
	var left = null;
	
	var init = (function(n){
		return function(){
			var cs = dojo.getComputedStyle(n);
			var pos = cs.position;
			top = (pos == 'absolute' ? n.offsetTop : parseInt(cs.top) || 0);
			left = (pos == 'absolute' ? n.offsetLeft : parseInt(cs.left) || 0);
			if(pos != 'absolute' && pos != 'relative'){
				var ret = dojo.coords(n, true);
				top = ret.y;
				left = ret.x;
				n.style.position="absolute";
				n.style.top=top+"px";
				n.style.left=left+"px";
			}
		};
	})(node);
	init();

	var anim = dojo.animateProperty(dojo.mixin({
		properties: {
			top: { end: args.top||0 },
			left: { end: args.left||0 }
		}
	}, args));
	dojo.connect(anim, "beforeBegin", anim, init);

	return anim; // dojo._Animation
}

}

if(!dojo._hasResource["dijit._base.focus"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base.focus"] = true;
dojo.provide("dijit._base.focus");

// summary:
//		These functions are used to query or set the focus and selection.
//
//		Also, they trace when widgets become actived/deactivated,
//		so that the widget can fire _onFocus/_onBlur events.
//		"Active" here means something similar to "focused", but
//		"focus" isn't quite the right word because we keep track of
//		a whole stack of "active" widgets.  Example:  Combobutton --> Menu -->
//		MenuItem.   The onBlur event for Combobutton doesn't fire due to focusing
//		on the Menu or a MenuItem, since they are considered part of the
//		Combobutton widget.  It only happens when focus is shifted
//		somewhere completely different.

dojo.mixin(dijit,
{
	// _curFocus: DomNode
	//		Currently focused item on screen
	_curFocus: null,

	// _prevFocus: DomNode
	//		Previously focused item on screen
	_prevFocus: null,

	isCollapsed: function(){
		// summary: tests whether the current selection is empty
		var _window = dojo.global;
		var _document = dojo.doc;
		if(_document.selection){ // IE
			return !_document.selection.createRange().text; // Boolean
		}else{
			var selection = _window.getSelection();
			if(dojo.isString(selection)){ // Safari
				return !selection; // Boolean
			}else{ // Mozilla/W3
				return selection.isCollapsed || !selection.toString(); // Boolean
			}
		}
	},

	getBookmark: function(){
		// summary: Retrieves a bookmark that can be used with moveToBookmark to return to the same range
		var bookmark, selection = dojo.doc.selection;
		if(selection){ // IE
			var range = selection.createRange();
			if(selection.type.toUpperCase()=='CONTROL'){
				if(range.length){
					bookmark=[];
					var i=0,len=range.length;
					while(i<len){
						bookmark.push(range.item(i++));
					}
				}else{
					bookmark=null;
				}
			}else{
				bookmark = range.getBookmark();
			}
		}else{
			if(window.getSelection){
				selection = dojo.global.getSelection();
				if(selection){
					range = selection.getRangeAt(0);
					bookmark = range.cloneRange();
				}
			}else{
				console.warn("No idea how to store the current selection for this browser!");
			}
		}
		return bookmark; // Array
	},

	moveToBookmark: function(/*Object*/bookmark){
		// summary: Moves current selection to a bookmark
		// bookmark: This should be a returned object from dojo.html.selection.getBookmark()
		var _document = dojo.doc;
		if(_document.selection){ // IE
			var range;
			if(dojo.isArray(bookmark)){
				range = _document.body.createControlRange();
				dojo.forEach(bookmark, "range.addElement(item)"); //range.addElement does not have call/apply method, so can not call it directly
			}else{
				range = _document.selection.createRange();
				range.moveToBookmark(bookmark);
			}
			range.select();
		}else{ //Moz/W3C
			var selection = dojo.global.getSelection && dojo.global.getSelection();
			if(selection && selection.removeAllRanges){
				selection.removeAllRanges();
				selection.addRange(bookmark);
			}else{
				console.warn("No idea how to restore selection for this browser!");
			}
		}
	},

	getFocus: function(/*Widget?*/menu, /*Window?*/openedForWindow){
		// summary:
		//	Returns the current focus and selection.
		//	Called when a popup appears (either a top level menu or a dialog),
		//	or when a toolbar/menubar receives focus
		//
		// menu:
		//	The menu that's being opened
		//
		// openedForWindow:
		//	iframe in which menu was opened
		//
		// returns:
		//	A handle to restore focus/selection

		return {
			// Node to return focus to
			node: menu && dojo.isDescendant(dijit._curFocus, menu.domNode) ? dijit._prevFocus : dijit._curFocus,

			// Previously selected text
			bookmark:
				!dojo.withGlobal(openedForWindow||dojo.global, dijit.isCollapsed) ?
				dojo.withGlobal(openedForWindow||dojo.global, dijit.getBookmark) :
				null,

			openedForWindow: openedForWindow
		}; // Object
	},

	focus: function(/*Object || DomNode */ handle){
		// summary:
		//		Sets the focused node and the selection according to argument.
		//		To set focus to an iframe's content, pass in the iframe itself.
		// handle:
		//		object returned by get(), or a DomNode

		if(!handle){ return; }

		var node = "node" in handle ? handle.node : handle,		// because handle is either DomNode or a composite object
			bookmark = handle.bookmark,
			openedForWindow = handle.openedForWindow;

		// Set the focus
		// Note that for iframe's we need to use the <iframe> to follow the parentNode chain,
		// but we need to set focus to iframe.contentWindow
		if(node){
			var focusNode = (node.tagName.toLowerCase()=="iframe") ? node.contentWindow : node;
			if(focusNode && focusNode.focus){
				try{
					// Gecko throws sometimes if setting focus is impossible,
					// node not displayed or something like that
					focusNode.focus();
				}catch(e){/*quiet*/}
			}			
			dijit._onFocusNode(node);
		}

		// set the selection
		// do not need to restore if current selection is not empty
		// (use keyboard to select a menu item)
		if(bookmark && dojo.withGlobal(openedForWindow||dojo.global, dijit.isCollapsed)){
			if(openedForWindow){
				openedForWindow.focus();
			}
			try{
				dojo.withGlobal(openedForWindow||dojo.global, dijit.moveToBookmark, null, [bookmark]);
			}catch(e){
				/*squelch IE internal error, see http://trac.dojotoolkit.org/ticket/1984 */
			}
		}
	},

	// _activeStack: Array
	//		List of currently active widgets (focused widget and it's ancestors)
	_activeStack: [],

	registerWin: function(/*Window?*/targetWindow){
		// summary:
		//		Registers listeners on the specified window (either the main
		//		window or an iframe) to detect when the user has clicked somewhere.
		//		Anyone that creates an iframe should call this function.

		if(!targetWindow){
			targetWindow = window;
		}

		dojo.connect(targetWindow.document, "onmousedown", function(evt){
			dijit._justMouseDowned = true;
			setTimeout(function(){ dijit._justMouseDowned = false; }, 0);
			dijit._onTouchNode(evt.target||evt.srcElement);
		});
		//dojo.connect(targetWindow, "onscroll", ???);

		// Listen for blur and focus events on targetWindow's body
		var body = targetWindow.document.body || targetWindow.document.getElementsByTagName("body")[0];
		if(body){
			if(dojo.isIE){
				body.attachEvent('onactivate', function(evt){
					if(evt.srcElement.tagName.toLowerCase() != "body"){
						dijit._onFocusNode(evt.srcElement);
					}
				});
				body.attachEvent('ondeactivate', function(evt){ dijit._onBlurNode(evt.srcElement); });
			}else{
				body.addEventListener('focus', function(evt){ dijit._onFocusNode(evt.target); }, true);
				body.addEventListener('blur', function(evt){ dijit._onBlurNode(evt.target); }, true);
			}
		}
		body = null;	// prevent memory leak (apparent circular reference via closure)
	},

	_onBlurNode: function(/*DomNode*/ node){
		// summary:
		// 		Called when focus leaves a node.
		//		Usually ignored, _unless_ it *isn't* follwed by touching another node,
		//		which indicates that we tabbed off the last field on the page,
		//		in which case every widget is marked inactive
		dijit._prevFocus = dijit._curFocus;
		dijit._curFocus = null;

		if(dijit._justMouseDowned){
			// the mouse down caused a new widget to be marked as active; this blur event
			// is coming late, so ignore it.
			return;
		}

		// if the blur event isn't followed by a focus event then mark all widgets as inactive.
		if(dijit._clearActiveWidgetsTimer){
			clearTimeout(dijit._clearActiveWidgetsTimer);
		}
		dijit._clearActiveWidgetsTimer = setTimeout(function(){
			delete dijit._clearActiveWidgetsTimer;
			dijit._setStack([]);
			dijit._prevFocus = null;
		}, 100);
	},

	_onTouchNode: function(/*DomNode*/ node){
		// summary:
		//		Callback when node is focused or mouse-downed

		// ignore the recent blurNode event
		if(dijit._clearActiveWidgetsTimer){
			clearTimeout(dijit._clearActiveWidgetsTimer);
			delete dijit._clearActiveWidgetsTimer;
		}

		// compute stack of active widgets (ex: ComboButton --> Menu --> MenuItem)
		var newStack=[];
		try{
			while(node){
				if(node.dijitPopupParent){
					node=dijit.byId(node.dijitPopupParent).domNode;
				}else if(node.tagName && node.tagName.toLowerCase()=="body"){
					// is this the root of the document or just the root of an iframe?
					if(node===dojo.body()){
						// node is the root of the main document
						break;
					}
					// otherwise, find the iframe this node refers to (can't access it via parentNode,
					// need to do this trick instead). window.frameElement is supported in IE/FF/Webkit
					node=dijit.getDocumentWindow(node.ownerDocument).frameElement;
				}else{
					var id = node.getAttribute && node.getAttribute("widgetId");
					if(id){
						newStack.unshift(id);
					}
					node=node.parentNode;
				}
			}
		}catch(e){ /* squelch */ }

		dijit._setStack(newStack);
	},

	_onFocusNode: function(/*DomNode*/ node){
		// summary
		//		Callback when node is focused
		if(node && node.tagName && node.tagName.toLowerCase() == "body"){
			return;
		}
		dijit._onTouchNode(node);

		if(node==dijit._curFocus){ return; }
		if(dijit._curFocus){
			dijit._prevFocus = dijit._curFocus;
		}
		dijit._curFocus = node;
		dojo.publish("focusNode", [node]);
	},

	_setStack: function(newStack){
		// summary
		//	The stack of active widgets has changed.  Send out appropriate events and record new stack

		var oldStack = dijit._activeStack;		
		dijit._activeStack = newStack;

		// compare old stack to new stack to see how many elements they have in common
		for(var nCommon=0; nCommon<Math.min(oldStack.length, newStack.length); nCommon++){
			if(oldStack[nCommon] != newStack[nCommon]){
				break;
			}
		}

		// for all elements that have gone out of focus, send blur event
		for(var i=oldStack.length-1; i>=nCommon; i--){
			var widget = dijit.byId(oldStack[i]);
			if(widget){
				widget._focused = false;
				widget._hasBeenBlurred = true;
				if(widget._onBlur){
					widget._onBlur();
				}
				if (widget._setStateClass){
					widget._setStateClass();
				}
				dojo.publish("widgetBlur", [widget]);
			}
		}

		// for all element that have come into focus, send focus event
		for(i=nCommon; i<newStack.length; i++){
			widget = dijit.byId(newStack[i]);
			if(widget){
				widget._focused = true;
				if(widget._onFocus){
					widget._onFocus();
				}
				if (widget._setStateClass){
					widget._setStateClass();
				}
				dojo.publish("widgetFocus", [widget]);
			}
		}
	}
});

// register top window and all the iframes it contains
dojo.addOnLoad(dijit.registerWin);

}

if(!dojo._hasResource["dijit._base.manager"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base.manager"] = true;
dojo.provide("dijit._base.manager");

dojo.declare("dijit.WidgetSet", null, {
	// summary:
	//	A set of widgets indexed by id

	constructor: function(){
		this._hash={};
	},

	add: function(/*Widget*/ widget){
		if(this._hash[widget.id]){
			throw new Error("Tried to register widget with id==" + widget.id + " but that id is already registered");
		}
		this._hash[widget.id]=widget;
	},

	remove: function(/*String*/ id){
		delete this._hash[id];
	},

	forEach: function(/*Function*/ func){
		for(var id in this._hash){
			func(this._hash[id]);
		}
	},

	filter: function(/*Function*/ filter){
		var res = new dijit.WidgetSet();
		this.forEach(function(widget){
			if(filter(widget)){ res.add(widget); }
		});
		return res;		// dijit.WidgetSet
	},

	byId: function(/*String*/ id){
		return this._hash[id];
	},

	byClass: function(/*String*/ cls){
		return this.filter(function(widget){ return widget.declaredClass==cls; });	// dijit.WidgetSet
	}
	});

/*=====
dijit.registry = {
	// summary: A list of widgets on a page.
	// description: Is an instance of dijit.WidgetSet
};
=====*/
dijit.registry = new dijit.WidgetSet();

dijit._widgetTypeCtr = {};

dijit.getUniqueId = function(/*String*/widgetType){
	// summary
	//	Generates a unique id for a given widgetType

	var id;
	do{
		id = widgetType + "_" +
			(widgetType in dijit._widgetTypeCtr ?
				++dijit._widgetTypeCtr[widgetType] : dijit._widgetTypeCtr[widgetType] = 0);
	}while(dijit.byId(id));
	return id; // String
};


if(dojo.isIE){
	// Only run this for IE because we think it's only necessary in that case,
	// and because it causes problems on FF.  See bug #3531 for details.
	dojo.addOnUnload(function(){
		dijit.registry.forEach(function(widget){ widget.destroy(); });
	});
}

dijit.byId = function(/*String|Widget*/id){
	// summary:
	//		Returns a widget by its id, or if passed a widget, no-op (like dojo.byId())
	return (dojo.isString(id)) ? dijit.registry.byId(id) : id; // Widget
};

dijit.byNode = function(/* DOMNode */ node){
	// summary:
	//		Returns the widget as referenced by node
	return dijit.registry.byId(node.getAttribute("widgetId")); // Widget
};

dijit.getEnclosingWidget = function(/* DOMNode */ node){
	// summary:
	//		Returns the widget whose dom tree contains node or null if
	//		the node is not contained within the dom tree of any widget
	while(node){
		if(node.getAttribute && node.getAttribute("widgetId")){
			return dijit.registry.byId(node.getAttribute("widgetId"));
		}
		node = node.parentNode;
	}
	return null;
};

// elements that are tab-navigable if they have no tabindex value set
// (except for "a", which must have an href attribute)
dijit._tabElements = {
	area: true,
	button: true,
	input: true,
	object: true,
	select: true,
	textarea: true
};

dijit._isElementShown = function(/*Element*/elem){
	var style = dojo.style(elem);
	return (style.visibility != "hidden")
		&& (style.visibility != "collapsed")
		&& (style.display != "none");
}

dijit.isTabNavigable = function(/*Element*/elem){
	// summary:
	//		Tests if an element is tab-navigable
	if(dojo.hasAttr(elem, "disabled")){ return false; }
	var hasTabindex = dojo.hasAttr(elem, "tabindex");
	var tabindex = dojo.attr(elem, "tabindex");
	if(hasTabindex && tabindex >= 0) {
		return true; // boolean
	}
	var name = elem.nodeName.toLowerCase();
	if(((name == "a" && dojo.hasAttr(elem, "href"))
			|| dijit._tabElements[name])
		&& (!hasTabindex || tabindex >= 0)){
		return true; // boolean
	}
	return false; // boolean
};

dijit._getTabNavigable = function(/*DOMNode*/root){
	// summary:
	//		Finds the following descendants of the specified root node:
	//		* the first tab-navigable element in document order
	//		  without a tabindex or with tabindex="0"
	//		* the last tab-navigable element in document order
	//		  without a tabindex or with tabindex="0"
	//		* the first element in document order with the lowest
	//		  positive tabindex value
	//		* the last element in document order with the highest
	//		  positive tabindex value
	var first, last, lowest, lowestTabindex, highest, highestTabindex;
	var walkTree = function(/*DOMNode*/parent){
		dojo.query("> *", parent).forEach(function(child){
			var isShown = dijit._isElementShown(child);
			if(isShown && dijit.isTabNavigable(child)){
				var tabindex = dojo.attr(child, "tabindex");
				if(!dojo.hasAttr(child, "tabindex") || tabindex == 0){
					if(!first){ first = child; }
					last = child;
				}else if(tabindex > 0){
					if(!lowest || tabindex < lowestTabindex){
						lowestTabindex = tabindex;
						lowest = child;
					}
					if(!highest || tabindex >= highestTabindex){
						highestTabindex = tabindex;
						highest = child;
					}
				}
			}
			if(isShown){ walkTree(child) }
		});
	};
	if(dijit._isElementShown(root)){ walkTree(root) }
	return { first: first, last: last, lowest: lowest, highest: highest };
}

dijit.getFirstInTabbingOrder = function(/*String|DOMNode*/root){
	// summary:
	//		Finds the descendant of the specified root node
	//		that is first in the tabbing order
	var elems = dijit._getTabNavigable(dojo.byId(root));
	return elems.lowest ? elems.lowest : elems.first; // Element
};

dijit.getLastInTabbingOrder = function(/*String|DOMNode*/root){
	// summary:
	//		Finds the descendant of the specified root node
	//		that is last in the tabbing order
	var elems = dijit._getTabNavigable(dojo.byId(root));
	return elems.last ? elems.last : elems.highest; // Element
};

}

if(!dojo._hasResource["dijit._base.place"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base.place"] = true;
dojo.provide("dijit._base.place");

// ported from dojo.html.util

dijit.getViewport = function(){
	//	summary
	//	Returns the dimensions and scroll position of the viewable area of a browser window

	var _window = dojo.global;
	var _document = dojo.doc;

	// get viewport size
	var w = 0, h = 0;
	var de = _document.documentElement;
	var dew = de.clientWidth, deh = de.clientHeight;
	if(dojo.isMozilla){
		// mozilla
		// _window.innerHeight includes the height taken by the scroll bar
		// clientHeight is ideal but has DTD issues:
		// #4539: FF reverses the roles of body.clientHeight/Width and documentElement.clientHeight/Width based on the DTD!
		// check DTD to see whether body or documentElement returns the viewport dimensions using this algorithm:
		var minw, minh, maxw, maxh;
		var dbw = _document.body.clientWidth;
		if(dbw > dew){
			minw = dew;
			maxw = dbw;
		}else{
			maxw = dew;
			minw = dbw;
		}
		var dbh = _document.body.clientHeight;
		if(dbh > deh){
			minh = deh;
			maxh = dbh;
		}else{
			maxh = deh;
			minh = dbh;
		}
		w = (maxw > _window.innerWidth) ? minw : maxw;
		h = (maxh > _window.innerHeight) ? minh : maxh;
	}else if(!dojo.isOpera && _window.innerWidth){
		//in opera9, dojo.body().clientWidth should be used, instead
		//of window.innerWidth/document.documentElement.clientWidth
		//so we have to check whether it is opera
		w = _window.innerWidth;
		h = _window.innerHeight;
	}else if(dojo.isIE && de && deh){
		w = dew;
		h = deh;
	}else if(dojo.body().clientWidth){
		// IE5, Opera
		w = dojo.body().clientWidth;
		h = dojo.body().clientHeight;
	}

	// get scroll position
	var scroll = dojo._docScroll();

	return { w: w, h: h, l: scroll.x, t: scroll.y };	//	object
};

dijit.placeOnScreen = function(
	/* DomNode */	node,
	/* Object */		pos,
	/* Object */		corners,
	/* boolean? */		tryOnly){
	//	summary:
	//		Keeps 'node' in the visible area of the screen while trying to
	//		place closest to pos.x, pos.y. The input coordinates are
	//		expected to be the desired document position.
	//
	//		Set which corner(s) you want to bind to, such as
	//		
	//			placeOnScreen(node, {x: 10, y: 20}, ["TR", "BL"])
	//		
	//		The desired x/y will be treated as the topleft(TL)/topright(TR) or
	//		BottomLeft(BL)/BottomRight(BR) corner of the node. Each corner is tested
	//		and if a perfect match is found, it will be used. Otherwise, it goes through
	//		all of the specified corners, and choose the most appropriate one.
	//		
	//		NOTE: node is assumed to be absolutely or relatively positioned.

	var choices = dojo.map(corners, function(corner){ return { corner: corner, pos: pos }; });

	return dijit._place(node, choices);
}

dijit._place = function(/*DomNode*/ node, /* Array */ choices, /* Function */ layoutNode){
	// summary:
	//		Given a list of spots to put node, put it at the first spot where it fits,
	//		of if it doesn't fit anywhere then the place with the least overflow
	// choices: Array
	//		Array of elements like: {corner: 'TL', pos: {x: 10, y: 20} }
	//		Above example says to put the top-left corner of the node at (10,20)
	//	layoutNode: Function(node, aroundNodeCorner, nodeCorner)
	//		for things like tooltip, they are displayed differently (and have different dimensions)
	//		based on their orientation relative to the parent.   This adjusts the popup based on orientation.

	// get {x: 10, y: 10, w: 100, h:100} type obj representing position of
	// viewport over document
	var view = dijit.getViewport();

	// This won't work if the node is inside a <div style="position: relative">,
	// so reattach it to dojo.doc.body.   (Otherwise, the positioning will be wrong
	// and also it might get cutoff)
	if(!node.parentNode || String(node.parentNode.tagName).toLowerCase() != "body"){
		dojo.body().appendChild(node);
	}

	var best = null;
	dojo.some(choices, function(choice){
		var corner = choice.corner;
		var pos = choice.pos;

		// configure node to be displayed in given position relative to button
		// (need to do this in order to get an accurate size for the node, because
		// a tooltips size changes based on position, due to triangle)
		if(layoutNode){
			layoutNode(node, choice.aroundCorner, corner);
		}

		// get node's size
		var style = node.style;
		var oldDisplay = style.display;
		var oldVis = style.visibility;
		style.visibility = "hidden";
		style.display = "";
		var mb = dojo.marginBox(node);
		style.display = oldDisplay;
		style.visibility = oldVis;

		// coordinates and size of node with specified corner placed at pos,
		// and clipped by viewport
		var startX = (corner.charAt(1) == 'L' ? pos.x : Math.max(view.l, pos.x - mb.w)),
			startY = (corner.charAt(0) == 'T' ? pos.y : Math.max(view.t, pos.y -  mb.h)),
			endX = (corner.charAt(1) == 'L' ? Math.min(view.l + view.w, startX + mb.w) : pos.x),
			endY = (corner.charAt(0) == 'T' ? Math.min(view.t + view.h, startY + mb.h) : pos.y),
			width = endX - startX,
			height = endY - startY,
			overflow = (mb.w - width) + (mb.h - height);

		if(best == null || overflow < best.overflow){
			best = {
				corner: corner,
				aroundCorner: choice.aroundCorner,
				x: startX,
				y: startY,
				w: width,
				h: height,
				overflow: overflow
			};
		}
		return !overflow;
	});

	node.style.left = best.x + "px";
	node.style.top = best.y + "px";
	if(best.overflow && layoutNode){
		layoutNode(node, best.aroundCorner, best.corner);
	}
	return best;
}

dijit.placeOnScreenAroundElement = function(
	/* DomNode */		node,
	/* DomNode */		aroundNode,
	/* Object */		aroundCorners,
	/* Function */		layoutNode){

	//	summary
	//	Like placeOnScreen, except it accepts aroundNode instead of x,y
	//	and attempts to place node around it.  Uses margin box dimensions.
	//
	//	aroundCorners
	//		specify Which corner of aroundNode should be
	//		used to place the node => which corner(s) of node to use (see the
	//		corners parameter in dijit.placeOnScreen)
	//		e.g. {'TL': 'BL', 'BL': 'TL'}
	//
	//	layoutNode: Function(node, aroundNodeCorner, nodeCorner)
	//		for things like tooltip, they are displayed differently (and have different dimensions)
	//		based on their orientation relative to the parent.   This adjusts the popup based on orientation.


	// get coordinates of aroundNode
	aroundNode = dojo.byId(aroundNode);
	var oldDisplay = aroundNode.style.display;
	aroundNode.style.display="";
	// #3172: use the slightly tighter border box instead of marginBox
	var aroundNodeW = aroundNode.offsetWidth; //mb.w;
	var aroundNodeH = aroundNode.offsetHeight; //mb.h;
	var aroundNodePos = dojo.coords(aroundNode, true);
	aroundNode.style.display=oldDisplay;

	// Generate list of possible positions for node
	var choices = [];
	for(var nodeCorner in aroundCorners){
		choices.push( {
			aroundCorner: nodeCorner,
			corner: aroundCorners[nodeCorner],
			pos: {
				x: aroundNodePos.x + (nodeCorner.charAt(1) == 'L' ? 0 : aroundNodeW),
				y: aroundNodePos.y + (nodeCorner.charAt(0) == 'T' ? 0 : aroundNodeH)
			}
		});
	}

	return dijit._place(node, choices, layoutNode);
}

}

if(!dojo._hasResource["dijit._base.window"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base.window"] = true;
dojo.provide("dijit._base.window");

dijit.getDocumentWindow = function(doc){
	//	summary
	// 	Get window object associated with document doc

	// With Safari, there is not way to retrieve the window from the document, so we must fix it.
	if(dojo.isSafari && !doc._parentWindow){
		/*
			This is a Safari specific function that fix the reference to the parent
			window from the document object.
			TODO: #5711: should the use of document below reference dojo.doc instead
			in case they're not the same?
		*/
		var fix=function(win){
			win.document._parentWindow=win;
			for(var i=0; i<win.frames.length; i++){
				fix(win.frames[i]);
			}
		}
		fix(window.top);
	}

	//In some IE versions (at least 6.0), document.parentWindow does not return a
	//reference to the real window object (maybe a copy), so we must fix it as well
	//We use IE specific execScript to attach the real window reference to
	//document._parentWindow for later use
	//TODO: #5711: should the use of document below reference dojo.doc instead in case they're not the same?
	if(dojo.isIE && window !== document.parentWindow && !doc._parentWindow){
		/*
		In IE 6, only the variable "window" can be used to connect events (others
		may be only copies).
		*/
		doc.parentWindow.execScript("document._parentWindow = window;", "Javascript");
		//to prevent memory leak, unset it after use
		//another possibility is to add an onUnload handler which seems overkill to me (liucougar)
		var win = doc._parentWindow;
		doc._parentWindow = null;
		return win;	//	Window
	}

	return doc._parentWindow || doc.parentWindow || doc.defaultView;	//	Window
}

}

if(!dojo._hasResource["dijit._base.popup"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base.popup"] = true;
dojo.provide("dijit._base.popup");





dijit.popup = new function(){
	// summary:
	//		This class is used to show/hide widgets as popups.
	//

	var stack = [],
		beginZIndex=1000,
		idGen = 1;

	this.prepare = function(/*DomNode*/ node){
		// summary:
		//		Prepares a node to be used as a popup
		//
		// description:
		//		Attaches node to dojo.doc.body, and
		//		positions it off screen, but not display:none, so that
		//		the widget doesn't appear in the page flow and/or cause a blank
		//		area at the bottom of the viewport (making scrollbar longer), but
		//		initialization of contained widgets works correctly
	
		dojo.body().appendChild(node);
		var s = node.style;
		if(s.display == "none"){
			s.display="";
		}
		s.visibility = "hidden";	// not needed for hiding, but used as flag that node is off-screen
		s.position = "absolute";
		s.top = "-9999px";
	};

	this.open = function(/*Object*/ args){
		// summary:
		//		Popup the widget at the specified position
		//
		// args: Object
		//		popup: Widget
		//			widget to display,
		//		parent: Widget
		//			the button etc. that is displaying this popup
		//		around: DomNode
		//			DOM node (typically a button); place popup relative to this node
		//		orient: Object
		//			structure specifying possible positions of popup relative to "around" node
		//		onCancel: Function
		//			callback when user has canceled the popup by
		//				1. hitting ESC or
		//				2. by using the popup widget's proprietary cancel mechanism (like a cancel button in a dialog);
		//				   ie: whenever popupWidget.onCancel() is called, args.onCancel is called
		//		onClose: Function
		//			callback whenever this popup is closed
		//		onExecute: Function
		//			callback when user "executed" on the popup/sub-popup by selecting a menu choice, etc. (top menu only)
		//
		// examples:
		//		1. opening at the mouse position
		//			dijit.popup.open({popup: menuWidget, x: evt.pageX, y: evt.pageY});
		//		2. opening the widget as a dropdown
		//			dijit.popup.open({parent: this, popup: menuWidget, around: this.domNode, onClose: function(){...}  });
		//
		//	Note that whatever widget called dijit.popup.open() should also listen to it's own _onBlur callback
		//	(fired from _base/focus.js) to know that focus has moved somewhere else and thus the popup should be closed.

		var widget = args.popup,
			orient = args.orient || {'BL':'TL', 'TL':'BL'},
			around = args.around,
			id = (args.around && args.around.id) ? (args.around.id+"_dropdown") : ("popup_"+idGen++);

		// make wrapper div to hold widget and possibly hold iframe behind it.
		// we can't attach the iframe as a child of the widget.domNode because
		// widget.domNode might be a <table>, <ul>, etc.
		var wrapper = dojo.doc.createElement("div");
		dijit.setWaiRole(wrapper, "presentation");
		wrapper.id = id;
		wrapper.className="dijitPopup";
		wrapper.style.zIndex = beginZIndex + stack.length;
		wrapper.style.visibility = "hidden";
		if(args.parent){
			wrapper.dijitPopupParent=args.parent.id;
		}
		dojo.body().appendChild(wrapper);

		var s = widget.domNode.style;
		s.display = "";
		s.visibility = "";
		s.position = "";
		wrapper.appendChild(widget.domNode);

		var iframe = new dijit.BackgroundIframe(wrapper);

		// position the wrapper node
		var best = around ?
			dijit.placeOnScreenAroundElement(wrapper, around, orient, widget.orient ? dojo.hitch(widget, "orient") : null) :
			dijit.placeOnScreen(wrapper, args, orient == 'R' ? ['TR','BR','TL','BL'] : ['TL','BL','TR','BR']);

		wrapper.style.visibility = "visible";
		// TODO: use effects to fade in wrapper

		var handlers = [];

		// Compute the closest ancestor popup that's *not* a child of another popup.
		// Ex: For a TooltipDialog with a button that spawns a tree of menus, find the popup of the button.
		var getTopPopup = function(){
			for(var pi=stack.length-1; pi > 0 && stack[pi].parent === stack[pi-1].widget; pi--){
				/* do nothing, just trying to get right value for pi */
			}
			return stack[pi];
		}

		// provide default escape and tab key handling
		// (this will work for any widget, not just menu)
		handlers.push(dojo.connect(wrapper, "onkeypress", this, function(evt){
			if(evt.keyCode == dojo.keys.ESCAPE && args.onCancel){
				dojo.stopEvent(evt);
				args.onCancel();
			}else if(evt.keyCode == dojo.keys.TAB){
				dojo.stopEvent(evt);
				var topPopup = getTopPopup();
				if(topPopup && topPopup.onCancel){
					topPopup.onCancel();
				}
			}
		}));

		// watch for cancel/execute events on the popup and notify the caller
		// (for a menu, "execute" means clicking an item)
		if(widget.onCancel){
			handlers.push(dojo.connect(widget, "onCancel", null, args.onCancel));
		}

		handlers.push(dojo.connect(widget, widget.onExecute ? "onExecute" : "onChange", null, function(){
			var topPopup = getTopPopup();
			if(topPopup && topPopup.onExecute){
				topPopup.onExecute();
			}
		}));

		stack.push({
			wrapper: wrapper,
			iframe: iframe,
			widget: widget,
			parent: args.parent,
			onExecute: args.onExecute,
			onCancel: args.onCancel,
 			onClose: args.onClose,
			handlers: handlers
		});

		if(widget.onOpen){
			widget.onOpen(best);
		}

		return best;
	};

	this.close = function(/*Widget*/ popup){
		// summary:
		//		Close specified popup and any popups that it parented
		while(dojo.some(stack, function(elem){return elem.widget == popup;})){
			var top = stack.pop(),
				wrapper = top.wrapper,
				iframe = top.iframe,
				widget = top.widget,
				onClose = top.onClose;
	
			if(widget.onClose){
				widget.onClose();
			}
			dojo.forEach(top.handlers, dojo.disconnect);
	
			// #2685: check if the widget still has a domNode so ContentPane can change its URL without getting an error
			if(!widget||!widget.domNode){ return; }
			
			this.prepare(widget.domNode);

			iframe.destroy();
			dojo._destroyElement(wrapper);
	
			if(onClose){
				onClose();
			}
		}
	};
}();

dijit._frames = new function(){
	// summary: cache of iframes
	var queue = [];

	this.pop = function(){
		var iframe;
		if(queue.length){
			iframe = queue.pop();
			iframe.style.display="";
		}else{
			if(dojo.isIE){
				var html="<iframe src='javascript:\"\"'"
					+ " style='position: absolute; left: 0px; top: 0px;"
					+ "z-index: -1; filter:Alpha(Opacity=\"0\");'>";
				iframe = dojo.doc.createElement(html);
			}else{
			 	iframe = dojo.doc.createElement("iframe");
				iframe.src = 'javascript:""';
				iframe.className = "dijitBackgroundIframe";
			}
			iframe.tabIndex = -1; // Magic to prevent iframe from getting focus on tab keypress - as style didnt work.
			dojo.body().appendChild(iframe);
		}
		return iframe;
	};

	this.push = function(iframe){
		iframe.style.display="";
		if(dojo.isIE){
			iframe.style.removeExpression("width");
			iframe.style.removeExpression("height");
		}
		queue.push(iframe);
	}
}();

// fill the queue
if(dojo.isIE && dojo.isIE < 7){
	dojo.addOnLoad(function(){
		var f = dijit._frames;
		dojo.forEach([f.pop()], f.push);
	});
}


dijit.BackgroundIframe = function(/* DomNode */node){
	//	summary:
	//		For IE z-index schenanigans. id attribute is required.
	//
	//	description:
	//		new dijit.BackgroundIframe(node)
	//			Makes a background iframe as a child of node, that fills
	//			area (and position) of node

	if(!node.id){ throw new Error("no id"); }
	if((dojo.isIE && dojo.isIE < 7) || (dojo.isFF && dojo.isFF < 3 && dojo.hasClass(dojo.body(), "dijit_a11y"))){
		var iframe = dijit._frames.pop();
		node.appendChild(iframe);
		if(dojo.isIE){
			iframe.style.setExpression("width", dojo._scopeName + ".doc.getElementById('" + node.id + "').offsetWidth");
			iframe.style.setExpression("height", dojo._scopeName + ".doc.getElementById('" + node.id + "').offsetHeight");
		}
		this.iframe = iframe;
	}
};

dojo.extend(dijit.BackgroundIframe, {
	destroy: function(){
		//	summary: destroy the iframe
		if(this.iframe){
			dijit._frames.push(this.iframe);
			delete this.iframe;
		}
	}
});

}

if(!dojo._hasResource["dijit._base.scroll"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base.scroll"] = true;
dojo.provide("dijit._base.scroll");

dijit.scrollIntoView = function(/* DomNode */node){
	//	summary
	//	Scroll the passed node into view, if it is not.

	// don't rely on that node.scrollIntoView works just because the function is there
	// it doesnt work in Konqueror or Opera even though the function is there and probably
	// not safari either
	// dont like browser sniffs implementations but sometimes you have to use it
	if(dojo.isMozilla){
		node.scrollIntoView(false);
	}else{
		// #6146: IE scrollIntoView is broken
		// It's not enough just to scroll the menu node into view if
		// node.scrollIntoView hides part of the parent's scrollbar,
		// so just manage the parent scrollbar ourselves
		var parent = node.parentNode;
		var parentBottom = parent.scrollTop + dojo.marginBox(parent).h; //PORT was getBorderBox
		var nodeBottom = node.offsetTop + dojo.marginBox(node).h;
		if(parentBottom < nodeBottom){
			parent.scrollTop += (nodeBottom - parentBottom);
		}else if(parent.scrollTop > node.offsetTop){
			parent.scrollTop -= (parent.scrollTop - node.offsetTop);
		}
	}
};

}

if(!dojo._hasResource["dijit._base.sniff"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base.sniff"] = true;
dojo.provide("dijit._base.sniff");

// ported from dojo.html.applyBrowserClass (style.js)

//	summary:
//		Applies pre-set class names based on browser & version to the
//		top-level HTML node.  Simply doing a require on this module will
//		establish this CSS.  Modified version of Morris' CSS hack.
(function(){
	var d = dojo;
	var ie = d.isIE;
	var opera = d.isOpera;
	var maj = Math.floor;
	var ff = d.isFF;
	var classes = {
		dj_ie: ie,
//		dj_ie55: ie == 5.5,
		dj_ie6: maj(ie) == 6,
		dj_ie7: maj(ie) == 7,
		dj_iequirks: ie && d.isQuirks,
// NOTE: Opera not supported by dijit
		dj_opera: opera,
		dj_opera8: maj(opera) == 8,
		dj_opera9: maj(opera) == 9,
		dj_khtml: d.isKhtml,
		dj_safari: d.isSafari,
		dj_gecko: d.isMozilla,
		dj_ff2: maj(ff) == 2
	}; // no dojo unsupported browsers

	for(var p in classes){
		if(classes[p]){
			var html = dojo.doc.documentElement; //TODO browser-specific DOM magic needed?
			if(html.className){
				html.className += " " + p;
			}else{
				html.className = p;
			}
		}
	}
})();

}

if(!dojo._hasResource["dijit._base.bidi"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base.bidi"] = true;
dojo.provide("dijit._base.bidi");

// summary: applies a class to the top of the document for right-to-left stylesheet rules

dojo.addOnLoad(function(){
	if(!dojo._isBodyLtr()){
		dojo.addClass(dojo.body(), "dijitRtl");
	}
});

}

if(!dojo._hasResource["dijit._base.typematic"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base.typematic"] = true;
dojo.provide("dijit._base.typematic");

dijit.typematic = {
	// summary:
	//	These functions are used to repetitively call a user specified callback
	//	method when a specific key or mouse click over a specific DOM node is
	//	held down for a specific amount of time.
	//	Only 1 such event is allowed to occur on the browser page at 1 time.

	_fireEventAndReload: function(){
		this._timer = null;
		this._callback(++this._count, this._node, this._evt);
		this._currentTimeout = (this._currentTimeout < 0) ? this._initialDelay : ((this._subsequentDelay > 1) ? this._subsequentDelay : Math.round(this._currentTimeout * this._subsequentDelay));
		this._timer = setTimeout(dojo.hitch(this, "_fireEventAndReload"), this._currentTimeout);
	},

	trigger: function(/*Event*/ evt, /* Object */ _this, /*DOMNode*/ node, /* Function */ callback, /* Object */ obj, /* Number */ subsequentDelay, /* Number */ initialDelay){
		// summary:
		//      Start a timed, repeating callback sequence.
		//      If already started, the function call is ignored.
		//      This method is not normally called by the user but can be
		//      when the normal listener code is insufficient.
		//	Parameters:
		//	evt: key or mouse event object to pass to the user callback
		//	_this: pointer to the user's widget space.
		//	node: the DOM node object to pass the the callback function
		//	callback: function to call until the sequence is stopped called with 3 parameters:
		//		count: integer representing number of repeated calls (0..n) with -1 indicating the iteration has stopped
		//		node: the DOM node object passed in
		//		evt: key or mouse event object
		//	obj: user space object used to uniquely identify each typematic sequence
		//	subsequentDelay: if > 1, the number of milliseconds until the 3->n events occur
		//		or else the fractional time multiplier for the next event's delay, default=0.9
		//	initialDelay: the number of milliseconds until the 2nd event occurs, default=500ms
		if(obj != this._obj){
			this.stop();
			this._initialDelay = initialDelay || 500;
			this._subsequentDelay = subsequentDelay || 0.90;
			this._obj = obj;
			this._evt = evt;
			this._node = node;
			this._currentTimeout = -1;
			this._count = -1;
			this._callback = dojo.hitch(_this, callback);
			this._fireEventAndReload();
		}
	},

	stop: function(){
		// summary:
		//	  Stop an ongoing timed, repeating callback sequence.
		if(this._timer){
			clearTimeout(this._timer);
			this._timer = null;
		}
		if(this._obj){
			this._callback(-1, this._node, this._evt);
			this._obj = null;
		}
	},

	addKeyListener: function(/*DOMNode*/ node, /*Object*/ keyObject, /*Object*/ _this, /*Function*/ callback, /*Number*/ subsequentDelay, /*Number*/ initialDelay){
		// summary: Start listening for a specific typematic key.
		//	keyObject: an object defining the key to listen for.
		//		key: (mandatory) the keyCode (number) or character (string) to listen for.
		//		ctrlKey: desired ctrl key state to initiate the calback sequence:
		//			pressed (true)
		//			released (false)
		//			either (unspecified)
		//		altKey: same as ctrlKey but for the alt key
		//		shiftKey: same as ctrlKey but for the shift key
		//	See the trigger method for other parameters.
		//	Returns an array of dojo.connect handles
		return [
			dojo.connect(node, "onkeypress", this, function(evt){
				if(evt.keyCode == keyObject.keyCode && (!keyObject.charCode || keyObject.charCode == evt.charCode) &&
				(keyObject.ctrlKey === undefined || keyObject.ctrlKey == evt.ctrlKey) &&
				(keyObject.altKey === undefined || keyObject.altKey == evt.ctrlKey) &&
				(keyObject.shiftKey === undefined || keyObject.shiftKey == evt.ctrlKey)){
					dojo.stopEvent(evt);
					dijit.typematic.trigger(keyObject, _this, node, callback, keyObject, subsequentDelay, initialDelay);
				}else if(dijit.typematic._obj == keyObject){
					dijit.typematic.stop();
				}
			}),
			dojo.connect(node, "onkeyup", this, function(evt){
				if(dijit.typematic._obj == keyObject){
					dijit.typematic.stop();
				}
			})
		];
	},

	addMouseListener: function(/*DOMNode*/ node, /*Object*/ _this, /*Function*/ callback, /*Number*/ subsequentDelay, /*Number*/ initialDelay){
		// summary: Start listening for a typematic mouse click.
		//	See the trigger method for other parameters.
		//	Returns an array of dojo.connect handles
		var dc = dojo.connect;
		return [
			dc(node, "mousedown", this, function(evt){
				dojo.stopEvent(evt);
				dijit.typematic.trigger(evt, _this, node, callback, node, subsequentDelay, initialDelay);
			}),
			dc(node, "mouseup", this, function(evt){
				dojo.stopEvent(evt);
				dijit.typematic.stop();
			}),
			dc(node, "mouseout", this, function(evt){
				dojo.stopEvent(evt);
				dijit.typematic.stop();
			}),
			dc(node, "mousemove", this, function(evt){
				dojo.stopEvent(evt);
			}),
			dc(node, "dblclick", this, function(evt){
				dojo.stopEvent(evt);
				if(dojo.isIE){
					dijit.typematic.trigger(evt, _this, node, callback, node, subsequentDelay, initialDelay);
					setTimeout(dojo.hitch(this, dijit.typematic.stop), 50);
				}
			})
		];
	},

	addListener: function(/*Node*/ mouseNode, /*Node*/ keyNode, /*Object*/ keyObject, /*Object*/ _this, /*Function*/ callback, /*Number*/ subsequentDelay, /*Number*/ initialDelay){
		// summary: Start listening for a specific typematic key and mouseclick.
		//	This is a thin wrapper to addKeyListener and addMouseListener.
		//	mouseNode: the DOM node object to listen on for mouse events.
		//	keyNode: the DOM node object to listen on for key events.
		//	See the addMouseListener and addKeyListener methods for other parameters.
		//	Returns an array of dojo.connect handles
		return this.addKeyListener(keyNode, keyObject, _this, callback, subsequentDelay, initialDelay).concat(
			this.addMouseListener(mouseNode, _this, callback, subsequentDelay, initialDelay));
	}
};

}

if(!dojo._hasResource["dijit._base.wai"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base.wai"] = true;
dojo.provide("dijit._base.wai");

dijit.wai = {
	onload: function(){
		// summary:
		//		Detects if we are in high-contrast mode or not

		// This must be a named function and not an anonymous
		// function, so that the widget parsing code can make sure it
		// registers its onload function after this function.
		// DO NOT USE "this" within this function.

		// create div for testing if high contrast mode is on or images are turned off
		var div = dojo.doc.createElement("div");
		div.id = "a11yTestNode";
		div.style.cssText = 'border: 1px solid;'
			+ 'border-color:red green;'
			+ 'position: absolute;'
			+ 'height: 5px;'
			+ 'top: -999px;'
			+ 'background-image: url("' + dojo.moduleUrl("dojo", "resources/blank.gif") + '");';
		dojo.body().appendChild(div);

		// test it
		var cs = dojo.getComputedStyle(div);
		if(cs){
			var bkImg = cs.backgroundImage;
			var needsA11y = (cs.borderTopColor==cs.borderRightColor) || (bkImg != null && (bkImg == "none" || bkImg == "url(invalid-url:)" ));
			dojo[needsA11y ? "addClass" : "removeClass"](dojo.body(), "dijit_a11y");
			dojo.body().removeChild(div);
		}
	}
};

// Test if computer is in high contrast mode.
// Make sure the a11y test runs first, before widgets are instantiated.
if(dojo.isIE || dojo.isMoz){	// NOTE: checking in Safari messes things up
	dojo._loaders.unshift(dijit.wai.onload);
}

dojo.mixin(dijit,
{
	hasWaiRole: function(/*Element*/ elem){
		// summary: Determines if an element has a role.
		// returns: true if elem has a role attribute and false if not.
		return elem.hasAttribute ? elem.hasAttribute("role") : !!elem.getAttribute("role");
	},

	getWaiRole: function(/*Element*/ elem){
		// summary: Gets the role for an element.
		// returns:
		//		The role of elem or an empty string if elem
		//		does not have a role.
		var value = elem.getAttribute("role");
		if(value){
			var prefixEnd = value.indexOf(":");
			return prefixEnd == -1 ? value : value.substring(prefixEnd+1);
		}else{
			return "";
		}
	},

	setWaiRole: function(/*Element*/ elem, /*String*/ role){
		// summary: Sets the role on an element.
		// description:
		//		On Firefox 2 and below, "wairole:" is
		//		prepended to the provided role value.
		elem.setAttribute("role", (dojo.isFF && dojo.isFF < 3) ? "wairole:" + role : role);
	},

	removeWaiRole: function(/*Element*/ elem){
		// summary: Removes the role from an element.
		elem.removeAttribute("role");
	},

	hasWaiState: function(/*Element*/ elem, /*String*/ state){
		// summary: Determines if an element has a given state.
		// description:
		//		On Firefox 2 and below, we check for an attribute in namespace
		//		"http://www.w3.org/2005/07/aaa" with a name of the given state.
		//		On all other browsers, we check for an attribute
		//		called "aria-"+state.
		// returns:
		//		true if elem has a value for the given state and
		//		false if it does not.
		if(dojo.isFF && dojo.isFF < 3){
			return elem.hasAttributeNS("http://www.w3.org/2005/07/aaa", state);
		}else{
			return elem.hasAttribute ? elem.hasAttribute("aria-"+state) : !!elem.getAttribute("aria-"+state);
		}
	},

	getWaiState: function(/*Element*/ elem, /*String*/ state){
		// summary: Gets the value of a state on an element.
		// description:
		//		On Firefox 2 and below, we check for an attribute in namespace
		//		"http://www.w3.org/2005/07/aaa" with a name of the given state.
		//		On all other browsers, we check for an attribute called
		//		"aria-"+state.
		// returns:
		//		The value of the requested state on elem
		//		or an empty string if elem has no value for state.
		if(dojo.isFF && dojo.isFF < 3){
			return elem.getAttributeNS("http://www.w3.org/2005/07/aaa", state);
		}else{
			var value = elem.getAttribute("aria-"+state);
			return value ? value : "";
		}
	},

	setWaiState: function(/*Element*/ elem, /*String*/ state, /*String*/ value){
		// summary: Sets a state on an element.
		// description:
		//		On Firefox 2 and below, we set an attribute in namespace
		//		"http://www.w3.org/2005/07/aaa" with a name of the given state.
		//		On all other browsers, we set an attribute called
		//		"aria-"+state.
		if(dojo.isFF && dojo.isFF < 3){
			elem.setAttributeNS("http://www.w3.org/2005/07/aaa",
				"aaa:"+state, value);
		}else{
			elem.setAttribute("aria-"+state, value);
		}
	},

	removeWaiState: function(/*Element*/ elem, /*String*/ state){
		// summary: Removes a state from an element.
		// description:
		//		On Firefox 2 and below, we remove the attribute in namespace
		//		"http://www.w3.org/2005/07/aaa" with a name of the given state.
		//		On all other browsers, we remove the attribute called
		//		"aria-"+state.
		if(dojo.isFF && dojo.isFF < 3){
			elem.removeAttributeNS("http://www.w3.org/2005/07/aaa", state);
		}else{
			elem.removeAttribute("aria-"+state);
		}
	}
});

}

if(!dojo._hasResource["dijit._base"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._base"] = true;
dojo.provide("dijit._base");












//	FIXME: Find a better way of solving this bug!
if(dojo.isSafari){
	//	Ugly-ass hack to solve bug #5626 for 1.1; basically force Safari to re-layout.
	//	Note that we can't reliably use dojo.addOnLoad here because this bug is basically
	//		a timing / race condition; so instead we use window.onload.
	dojo.connect(window, "load", function(){
		window.resizeBy(1,0);
		setTimeout(function(){ window.resizeBy(-1,0); }, 10);
	});
}

}

if(!dojo._hasResource["dijit._Widget"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._Widget"] = true;
dojo.provide("dijit._Widget");

dojo.require( "dijit._base" );

dojo.declare("dijit._Widget", null, {
	//	summary:
	//		The foundation of dijit widgets. 	
	//
	//	id: String
	//		a unique, opaque ID string that can be assigned by users or by the
	//		system. If the developer passes an ID which is known not to be
	//		unique, the specified ID is ignored and the system-generated ID is
	//		used instead.
	id: "",

	//	lang: String
	//		Rarely used.  Overrides the default Dojo locale used to render this widget,
	//		as defined by the [HTML LANG](http://www.w3.org/TR/html401/struct/dirlang.html#adef-lang) attribute.
	//		Value must be among the list of locales specified during by the Dojo bootstrap,
	//		formatted according to [RFC 3066](http://www.ietf.org/rfc/rfc3066.txt) (like en-us).
	lang: "",

	//	dir: String
	//		Unsupported by Dijit, but here for completeness.  Dijit only supports setting text direction on the
	//		entire document.
	//		Bi-directional support, as defined by the [HTML DIR](http://www.w3.org/TR/html401/struct/dirlang.html#adef-dir)
	//		attribute. Either left-to-right "ltr" or right-to-left "rtl".
	dir: "",

	// class: String
	//		HTML class attribute
	"class": "",

	// style: String
	//		HTML style attribute
	style: "",

	// title: String
	//		HTML title attribute
	title: "",

	// srcNodeRef: DomNode
	//		pointer to original dom node
	srcNodeRef: null,

	// domNode: DomNode
	//		this is our visible representation of the widget! Other DOM
	//		Nodes may by assigned to other properties, usually through the
	//		template system's dojoAttachPonit syntax, but the domNode
	//		property is the canonical "top level" node in widget UI.
	domNode: null,

	// attributeMap: Object
	//		A map of attributes and attachpoints -- typically standard HTML attributes -- to set
	//		on the widget's dom, at the "domNode" attach point, by default.
	//		Other node references can be specified as properties of 'this'
	attributeMap: {id:"", dir:"", lang:"", "class":"", style:"", title:""},  // TODO: add on* handlers?

	//////////// INITIALIZATION METHODS ///////////////////////////////////////
//TODOC: params and srcNodeRef need docs.  Is srcNodeRef optional?
//TODOC: summary needed for postscript
	postscript: function(/*Object?*/params, /*DomNode|String*/srcNodeRef){
		this.create(params, srcNodeRef);
	},

	create: function(/*Object?*/params, /*DomNode|String*/srcNodeRef){
		//	summary:
		//		Kick off the life-cycle of a widget
		//	description:
		//		To understand the process by which widgets are instantiated, it
		//		is critical to understand what other methods create calls and
		//		which of them you'll want to override. Of course, adventurous
		//		developers could override create entirely, but this should
		//		only be done as a last resort.
		//
		//		Below is a list of the methods that are called, in the order
		//		they are fired, along with notes about what they do and if/when
		//		you should over-ride them in your widget:
		//
		// * postMixInProperties:
		//	|	* a stub function that you can over-ride to modify
		//		variables that may have been naively assigned by
		//		mixInProperties
		// * widget is added to manager object here
		// * buildRendering:
		//	|	* Subclasses use this method to handle all UI initialization
		//		Sets this.domNode.  Templated widgets do this automatically
		//		and otherwise it just uses the source dom node.
		// * postCreate:
		//	|	* a stub function that you can over-ride to modify take
		//		actions once the widget has been placed in the UI

		// store pointer to original dom tree
		this.srcNodeRef = dojo.byId(srcNodeRef);

		// For garbage collection.  An array of handles returned by Widget.connect()
		// Each handle returned from Widget.connect() is an array of handles from dojo.connect()
		this._connects=[];

		// _attaches: String[]
		// 		names of all our dojoAttachPoint variables
		this._attaches=[];

		//mixin our passed parameters
		if(this.srcNodeRef && (typeof this.srcNodeRef.id == "string")){ this.id = this.srcNodeRef.id; }
		if(params){
			this.params = params;
			dojo.mixin(this,params);
		}
		this.postMixInProperties();

		// generate an id for the widget if one wasn't specified
		// (be sure to do this before buildRendering() because that function might
		// expect the id to be there.
		if(!this.id){
			this.id=dijit.getUniqueId(this.declaredClass.replace(/\./g,"_"));
		}
		dijit.registry.add(this);

		this.buildRendering();

		// Copy attributes listed in attributeMap into the [newly created] DOM for the widget.
		// The placement of these attributes is according to the property mapping in attributeMap.
		// Note special handling for 'style' and 'class' attributes which are lists and can
		// have elements from both old and new structures, and some attributes like "type"
		// cannot be processed this way as they are not mutable.
		if(this.domNode){
			for(var attr in this.attributeMap){
				var value = this[attr];
				if(typeof value != "object" && ((value !== "" && value !== false) || (params && params[attr]))){
					this.setAttribute(attr, value);
				}
			}
		}

		if(this.domNode){
			this.domNode.setAttribute("widgetId", this.id);
		}
		this.postCreate();

		// If srcNodeRef has been processed and removed from the DOM (e.g. TemplatedWidget) then delete it to allow GC.
		if(this.srcNodeRef && !this.srcNodeRef.parentNode){
			delete this.srcNodeRef;
		}	
	},

	postMixInProperties: function(){
		// summary
		//	Called after the parameters to the widget have been read-in,
		//	but before the widget template is instantiated.
		//	Especially useful to set properties that are referenced in the widget template.
	},

	buildRendering: function(){
		// summary:
		//		Construct the UI for this widget, setting this.domNode.
		//		Most widgets will mixin TemplatedWidget, which overrides this method.
		this.domNode = this.srcNodeRef || dojo.doc.createElement('div');
	},

	postCreate: function(){
		// summary:
		//		Called after a widget's dom has been setup
	},

	startup: function(){
		// summary:
		//		Called after a widget's children, and other widgets on the page, have been created.
		//		Provides an opportunity to manipulate any children before they are displayed.
		//		This is useful for composite widgets that need to control or layout sub-widgets.
		//		Many layout widgets can use this as a wiring phase.
		this._started = true;
	},

	//////////// DESTROY FUNCTIONS ////////////////////////////////

	destroyRecursive: function(/*Boolean*/ finalize){
		// summary:
		// 		Destroy this widget and it's descendants. This is the generic
		// 		"destructor" function that all widget users should call to
		// 		cleanly discard with a widget. Once a widget is destroyed, it's
		// 		removed from the manager object.
		// finalize: Boolean
		//		is this function being called part of global environment
		//		tear-down?

		this.destroyDescendants();
		this.destroy();
	},

	destroy: function(/*Boolean*/ finalize){
		// summary:
		// 		Destroy this widget, but not its descendants
		// finalize: Boolean
		//		is this function being called part of global environment
		//		tear-down?

		this.uninitialize();
		dojo.forEach(this._connects, function(array){
			dojo.forEach(array, dojo.disconnect);
		});

		// destroy widgets created as part of template, etc.
		dojo.forEach(this._supportingWidgets || [], function(w){ w.destroy(); });
		
		this.destroyRendering(finalize);
		dijit.registry.remove(this.id);
	},

	destroyRendering: function(/*Boolean*/ finalize){
		// summary:
		//		Destroys the DOM nodes associated with this widget
		// finalize: Boolean
		//		is this function being called part of global environment
		//		tear-down?

		if(this.bgIframe){
			this.bgIframe.destroy();
			delete this.bgIframe;
		}

		if(this.domNode){
			dojo._destroyElement(this.domNode);
			delete this.domNode;
		}

		if(this.srcNodeRef){
			dojo._destroyElement(this.srcNodeRef);
			delete this.srcNodeRef;
		}
	},

	destroyDescendants: function(){
		// summary:
		//		Recursively destroy the children of this widget and their
		//		descendants.

		// TODO: should I destroy in the reverse order, to go bottom up?
		dojo.forEach(this.getDescendants(), function(widget){ widget.destroy(); });
	},

	uninitialize: function(){
		// summary:
		//		stub function. Override to implement custom widget tear-down
		//		behavior.
		return false;
	},

	////////////////// MISCELLANEOUS METHODS ///////////////////

	onFocus: function(){
		// summary:
		//		stub function. Override or connect to this method to receive
		//		notifications for when the widget moves into focus.
	},

	onBlur: function(){
		// summary:
		//		stub function. Override or connect to this method to receive
		//		notifications for when the widget moves out of focus.
	},

	_onFocus: function(e){
		this.onFocus();
	},

	_onBlur: function(){
		this.onBlur();
	},

	setAttribute: function(/*String*/ attr, /*anything*/ value){
		// summary
		//		Set native HTML attributes reflected in the widget,
		//		such as readOnly, disabled, and maxLength in TextBox widgets.
		// description
		//		In general, a widget's "value" is controlled via setValue()/getValue(), 
		//		rather than this method.  The exception is for widgets where the
		//		end user can't adjust the value, such as Button and CheckBox;
		//		in the unusual case that you want to change the value attribute of
		//		those widgets, use setAttribute().
		var mapNode = this[this.attributeMap[attr]||'domNode'];
		this[attr] = value;
		switch(attr){
			case "class":
				dojo.addClass(mapNode, value);
				break;
			case "style":
				if(mapNode.style.cssText){
					mapNode.style.cssText += "; " + value;// FIXME: Opera
				}else{
					mapNode.style.cssText = value;
				}
				break;
			default:
				if(/^on[A-Z]/.test(attr)){ // eg. onSubmit needs to be onsubmit
					attr = attr.toLowerCase();
				}
				if(typeof value == "function"){ // functions execute in the context of the widget
					value = dojo.hitch(this, value);
				}
				dojo.attr(mapNode, attr, value);
		}
	},

	toString: function(){
		// summary:
		//		returns a string that represents the widget. When a widget is
		//		cast to a string, this method will be used to generate the
		//		output. Currently, it does not implement any sort of reversable
		//		serialization.
		return '[Widget ' + this.declaredClass + ', ' + (this.id || 'NO ID') + ']'; // String
	},

	getDescendants: function(){
		// summary:
		//	Returns all the widgets that contained by this, i.e., all widgets underneath this.containerNode.
		if(this.containerNode){
			var list= dojo.query('[widgetId]', this.containerNode);
			return list.map(dijit.byNode);		// Array
		}else{
			return [];
		}
	},

//TODOC
	nodesWithKeyClick: ["input", "button"],

	connect: function(
			/*Object|null*/ obj,
			/*String*/ event,
			/*String|Function*/ method){
		//	summary:
		//		Connects specified obj/event to specified method of this object
		//		and registers for disconnect() on widget destroy.
		//		Special event: "ondijitclick" triggers on a click or enter-down or space-up
		//		Similar to dojo.connect() but takes three arguments rather than four.
		var handles =[];
		if(event == "ondijitclick"){
			// add key based click activation for unsupported nodes.
			if(!this.nodesWithKeyClick[obj.nodeName]){
				handles.push(dojo.connect(obj, "onkeydown", this,
					function(e){
						if(e.keyCode == dojo.keys.ENTER){
							return (dojo.isString(method))?
								this[method](e) : method.call(this, e);
						}else if(e.keyCode == dojo.keys.SPACE){
							// stop space down as it causes IE to scroll
							// the browser window
							dojo.stopEvent(e);
						}
			 		}));
				handles.push(dojo.connect(obj, "onkeyup", this,
					function(e){
						if(e.keyCode == dojo.keys.SPACE){
							return dojo.isString(method) ?
								this[method](e) : method.call(this, e);
						}
			 		}));
			}
			event = "onclick";
		}
		handles.push(dojo.connect(obj, event, this, method));

		// return handles for FormElement and ComboBox
		this._connects.push(handles);
		return handles;
	},

	disconnect: function(/*Object*/ handles){
		// summary:
		//		Disconnects handle created by this.connect.
		//		Also removes handle from this widget's list of connects
		for(var i=0; i<this._connects.length; i++){
			if(this._connects[i]==handles){
				dojo.forEach(handles, dojo.disconnect);
				this._connects.splice(i, 1);
				return;
			}
		}
	},

	isLeftToRight: function(){
		// summary:
		//		Checks the DOM to for the text direction for bi-directional support
		// description:
		//		This method cannot be used during widget construction because the widget
		//		must first be connected to the DOM tree.  Parent nodes are searched for the
		//		'dir' attribute until one is found, otherwise left to right mode is assumed.
		//		See HTML spec, DIR attribute for more information.

		if(!("_ltr" in this)){
			this._ltr = dojo.getComputedStyle(this.domNode).direction != "rtl";
		}
		return this._ltr; //Boolean
	},

	isFocusable: function(){
		// summary:
		//		Return true if this widget can currently be focused
		//		and false if not
		return this.focus && (dojo.style(this.domNode, "display") != "none");
	}
});

}

if(!dojo._hasResource["dojo.string"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.string"] = true;
dojo.provide("dojo.string");

/*=====
dojo.string = { 
	// summary: String utilities for Dojo
};
=====*/

dojo.string.pad = function(/*String*/text, /*int*/size, /*String?*/ch, /*boolean?*/end){
	// summary:
	//		Pad a string to guarantee that it is at least `size` length by
	//		filling with the character `ch` at either the start or end of the
	//		string. Pads at the start, by default.
	// text: the string to pad
	// size: length to provide padding
	// ch: character to pad, defaults to '0'
	// end: adds padding at the end if true, otherwise pads at start

	var out = String(text);
	if(!ch){
		ch = '0';
	}
	while(out.length < size){
		if(end){
			out += ch;
		}else{
			out = ch + out;
		}
	}
	return out;	// String
};

dojo.string.substitute = function(	/*String*/template, 
									/*Object|Array*/map, 
									/*Function?*/transform, 
									/*Object?*/thisObject){
	// summary:
	//		Performs parameterized substitutions on a string. Throws an
	//		exception if any parameter is unmatched.
	// description:
	//		For example,
	//		|	dojo.string.substitute("File '${0}' is not found in directory '${1}'.",["foo.html","/temp"]);
	//		|	dojo.string.substitute("File '${name}' is not found in directory '${info.dir}'.",
	//		|		{name: "foo.html", info: {dir: "/temp"}});
	//		both return
	//		|	"File 'foo.html' is not found in directory '/temp'."
	// template: 
	//		a string with expressions in the form `${key}` to be replaced or
	//		`${key:format}` which specifies a format function.
	// map: hash to search for substitutions
	// transform: 
	//		a function to process all parameters before substitution takes
	//		place, e.g. dojo.string.encodeXML
	// thisObject: 
	//		where to look for optional format function; default to the global
	//		namespace

	return template.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g, function(match, key, format){
		var value = dojo.getObject(key,false,map);
		if(format){ value = dojo.getObject(format,false,thisObject)(value);}
		if(transform){ value = transform(value, key); }
		return value.toString();
	}); // string
};

dojo.string.trim = function(/*String*/ str){
	// summary: trims whitespaces from both sides of the string
	// description:
	//	This version of trim() was taken from [Steven Levithan's blog](http://blog.stevenlevithan.com/archives/faster-trim-javascript).
	//	The short yet performant version of this function is 
	//	dojo.trim(), which is part of Dojo base.
	str = str.replace(/^\s+/, '');
	for(var i = str.length - 1; i > 0; i--){
		if(/\S/.test(str.charAt(i))){
			str = str.substring(0, i + 1);
			break;
		}
	}
	return str;	// String
};

}

if(!dojo._hasResource["dojo.date.stamp"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.date.stamp"] = true;
dojo.provide("dojo.date.stamp");

// Methods to convert dates to or from a wire (string) format using well-known conventions

dojo.date.stamp.fromISOString = function(/*String*/formattedString, /*Number?*/defaultTime){
	//	summary:
	//		Returns a Date object given a string formatted according to a subset of the ISO-8601 standard.
	//
	//	description:
	//		Accepts a string formatted according to a profile of ISO8601 as defined by
	//		[RFC3339](http://www.ietf.org/rfc/rfc3339.txt), except that partial input is allowed.
	//		Can also process dates as specified [by the W3C](http://www.w3.org/TR/NOTE-datetime)
	//		The following combinations are valid:
	//
	//			* dates only
	//			|	* yyyy
	//			|	* yyyy-MM
	//			|	* yyyy-MM-dd
	// 			* times only, with an optional time zone appended
	//			|	* THH:mm
	//			|	* THH:mm:ss
	//			|	* THH:mm:ss.SSS
	// 			* and "datetimes" which could be any combination of the above
	//
	//		timezones may be specified as Z (for UTC) or +/- followed by a time expression HH:mm
	//		Assumes the local time zone if not specified.  Does not validate.  Improperly formatted
	//		input may return null.  Arguments which are out of bounds will be handled
	// 		by the Date constructor (e.g. January 32nd typically gets resolved to February 1st)
	//		Only years between 100 and 9999 are supported.
	//
  	//	formattedString:
	//		A string such as 2005-06-30T08:05:00-07:00 or 2005-06-30 or T08:05:00
	//
	//	defaultTime:
	//		Used for defaults for fields omitted in the formattedString.
	//		Uses 1970-01-01T00:00:00.0Z by default.

	if(!dojo.date.stamp._isoRegExp){
		dojo.date.stamp._isoRegExp =
//TODO: could be more restrictive and check for 00-59, etc.
			/^(?:(\d{4})(?:-(\d{2})(?:-(\d{2}))?)?)?(?:T(\d{2}):(\d{2})(?::(\d{2})(.\d+)?)?((?:[+-](\d{2}):(\d{2}))|Z)?)?$/;
	}

	var match = dojo.date.stamp._isoRegExp.exec(formattedString);
	var result = null;

	if(match){
		match.shift();
		if(match[1]){match[1]--;} // Javascript Date months are 0-based
		if(match[6]){match[6] *= 1000;} // Javascript Date expects fractional seconds as milliseconds

		if(defaultTime){
			// mix in defaultTime.  Relatively expensive, so use || operators for the fast path of defaultTime === 0
			defaultTime = new Date(defaultTime);
			dojo.map(["FullYear", "Month", "Date", "Hours", "Minutes", "Seconds", "Milliseconds"], function(prop){
				return defaultTime["get" + prop]();
			}).forEach(function(value, index){
				if(match[index] === undefined){
					match[index] = value;
				}
			});
		}
		result = new Date(match[0]||1970, match[1]||0, match[2]||1, match[3]||0, match[4]||0, match[5]||0, match[6]||0);
//		result.setFullYear(match[0]||1970); // for year < 100

		var offset = 0;
		var zoneSign = match[7] && match[7].charAt(0);
		if(zoneSign != 'Z'){
			offset = ((match[8] || 0) * 60) + (Number(match[9]) || 0);
			if(zoneSign != '-'){ offset *= -1; }
		}
		if(zoneSign){
			offset -= result.getTimezoneOffset();
		}
		if(offset){
			result.setTime(result.getTime() + offset * 60000);
		}
	}

	return result; // Date or null
}

/*=====
	dojo.date.stamp.__Options = function(){
		//	selector: String
		//		"date" or "time" for partial formatting of the Date object.
		//		Both date and time will be formatted by default.
		//	zulu: Boolean
		//		if true, UTC/GMT is used for a timezone
		//	milliseconds: Boolean
		//		if true, output milliseconds
		this.selector = selector;
		this.zulu = zulu;
		this.milliseconds = milliseconds;
	}
=====*/

dojo.date.stamp.toISOString = function(/*Date*/dateObject, /*dojo.date.stamp.__Options?*/options){
	//	summary:
	//		Format a Date object as a string according a subset of the ISO-8601 standard
	//
	//	description:
	//		When options.selector is omitted, output follows [RFC3339](http://www.ietf.org/rfc/rfc3339.txt)
	//		The local time zone is included as an offset from GMT, except when selector=='time' (time without a date)
	//		Does not check bounds.  Only years between 100 and 9999 are supported.
	//
	//	dateObject:
	//		A Date object

	var _ = function(n){ return (n < 10) ? "0" + n : n; };
	options = options || {};
	var formattedDate = [];
	var getter = options.zulu ? "getUTC" : "get";
	var date = "";
	if(options.selector != "time"){
		var year = dateObject[getter+"FullYear"]();
		date = ["0000".substr((year+"").length)+year, _(dateObject[getter+"Month"]()+1), _(dateObject[getter+"Date"]())].join('-');
	}
	formattedDate.push(date);
	if(options.selector != "date"){
		var time = [_(dateObject[getter+"Hours"]()), _(dateObject[getter+"Minutes"]()), _(dateObject[getter+"Seconds"]())].join(':');
		var millis = dateObject[getter+"Milliseconds"]();
		if(options.milliseconds){
			time += "."+ (millis < 100 ? "0" : "") + _(millis);
		}
		if(options.zulu){
			time += "Z";
		}else if(options.selector != "time"){
			var timezoneOffset = dateObject.getTimezoneOffset();
			var absOffset = Math.abs(timezoneOffset);
			time += (timezoneOffset > 0 ? "-" : "+") + 
				_(Math.floor(absOffset/60)) + ":" + _(absOffset%60);
		}
		formattedDate.push(time);
	}
	return formattedDate.join('T'); // String
}

}

if(!dojo._hasResource["dojo.parser"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.parser"] = true;
dojo.provide("dojo.parser");


dojo.parser = new function(){
	// summary: The Dom/Widget parsing package

	var d = dojo;
	var dtName = d._scopeName + "Type";
	var qry = "[" + dtName + "]";

	function val2type(/*Object*/ value){
		// summary:
		//		Returns name of type of given value.

		if(d.isString(value)){ return "string"; }
		if(typeof value == "number"){ return "number"; }
		if(typeof value == "boolean"){ return "boolean"; }
		if(d.isFunction(value)){ return "function"; }
		if(d.isArray(value)){ return "array"; } // typeof [] == "object"
		if(value instanceof Date) { return "date"; } // assume timestamp
		if(value instanceof d._Url){ return "url"; }
		return "object";
	}

	function str2obj(/*String*/ value, /*String*/ type){
		// summary:
		//		Convert given string value to given type
		switch(type){
			case "string":
				return value;
			case "number":
				return value.length ? Number(value) : NaN;
			case "boolean":
				// for checked/disabled value might be "" or "checked".  interpret as true.
				return typeof value == "boolean" ? value : !(value.toLowerCase()=="false");
			case "function":
				if(d.isFunction(value)){
					// IE gives us a function, even when we say something like onClick="foo"
					// (in which case it gives us an invalid function "function(){ foo }"). 
					//  Therefore, convert to string
					value=value.toString();
					value=d.trim(value.substring(value.indexOf('{')+1, value.length-1));
				}
				try{
					if(value.search(/[^\w\.]+/i) != -1){
						// TODO: "this" here won't work
						value = d.parser._nameAnonFunc(new Function(value), this);
					}
					return d.getObject(value, false);
				}catch(e){ return new Function(); }
			case "array":
				return value.split(/\s*,\s*/);
			case "date":
				switch(value){
					case "": return new Date("");	// the NaN of dates
					case "now": return new Date();	// current date
					default: return d.date.stamp.fromISOString(value);
				}
			case "url":
				return d.baseUrl + value;
			default:
				return d.fromJson(value);
		}
	}

	var instanceClasses = {
		// map from fully qualified name (like "dijit.Button") to structure like
		// { cls: dijit.Button, params: {label: "string", disabled: "boolean"} }
	};
	
	function getClassInfo(/*String*/ className){
		// className:
		//		fully qualified name (like "dijit.Button")
		// returns:
		//		structure like
		//			{ 
		//				cls: dijit.Button, 
		//				params: { label: "string", disabled: "boolean"}
		//			}

		if(!instanceClasses[className]){
			// get pointer to widget class
			var cls = d.getObject(className);
			if(!d.isFunction(cls)){
				throw new Error("Could not load class '" + className +
					"'. Did you spell the name correctly and use a full path, like 'dijit.form.Button'?");
			}
			var proto = cls.prototype;
	
			// get table of parameter names & types
			var params={};
			for(var name in proto){
				if(name.charAt(0)=="_"){ continue; } 	// skip internal properties
				var defVal = proto[name];
				params[name]=val2type(defVal);
			}

			instanceClasses[className] = { cls: cls, params: params };
		}
		return instanceClasses[className];
	}

	this._functionFromScript = function(script){
		var preamble = "";
		var suffix = "";
		var argsStr = script.getAttribute("args");
		if(argsStr){
			d.forEach(argsStr.split(/\s*,\s*/), function(part, idx){
				preamble += "var "+part+" = arguments["+idx+"]; ";
			});
		}
		var withStr = script.getAttribute("with");
		if(withStr && withStr.length){
			d.forEach(withStr.split(/\s*,\s*/), function(part){
				preamble += "with("+part+"){";
				suffix += "}";
			});
		}
		return new Function(preamble+script.innerHTML+suffix);
	}

	this.instantiate = function(/* Array */nodes){
		// summary:
		//		Takes array of nodes, and turns them into class instances and
		//		potentially calls a layout method to allow them to connect with
		//		any children		
		var thelist = [];
		d.forEach(nodes, function(node){
			if(!node){ return; }
			var type = node.getAttribute(dtName);
			if((!type)||(!type.length)){ return; }
			var clsInfo = getClassInfo(type);
			var clazz = clsInfo.cls;
			var ps = clazz._noScript||clazz.prototype._noScript;

			// read parameters (ie, attributes).
			// clsInfo.params lists expected params like {"checked": "boolean", "n": "number"}
			var params = {};
			var attributes = node.attributes;
			for(var name in clsInfo.params){
				var item = attributes.getNamedItem(name);
				if(!item || (!item.specified && (!dojo.isIE || name.toLowerCase()!="value"))){ continue; }
				var value = item.value;
				// Deal with IE quirks for 'class' and 'style'
				switch(name){
				case "class":
					value = node.className;
					break;
				case "style":
					value = node.style && node.style.cssText; // FIXME: Opera?
				}
				var _type = clsInfo.params[name];
				params[name] = str2obj(value, _type);
			}

			// Process <script type="dojo/*"> script tags
			// <script type="dojo/method" event="foo"> tags are added to params, and passed to
			// the widget on instantiation.
			// <script type="dojo/method"> tags (with no event) are executed after instantiation
			// <script type="dojo/connect" event="foo"> tags are dojo.connected after instantiation
			// note: dojo/* script tags cannot exist in self closing widgets, like <input />
			if(!ps){
				var connects = [],	// functions to connect after instantiation
					calls = [];		// functions to call after instantiation

				d.query("> script[type^='dojo/']", node).orphan().forEach(function(script){
					var event = script.getAttribute("event"),
						type = script.getAttribute("type"),
						nf = d.parser._functionFromScript(script);
					if(event){
						if(type == "dojo/connect"){
							connects.push({event: event, func: nf});
						}else{
							params[event] = nf;
						}
					}else{
						calls.push(nf);
					}
				});
			}

			var markupFactory = clazz["markupFactory"];
			if(!markupFactory && clazz["prototype"]){
				markupFactory = clazz.prototype["markupFactory"];
			}
			// create the instance
			var instance = markupFactory ? markupFactory(params, node, clazz) : new clazz(params, node);
			thelist.push(instance);

			// map it to the JS namespace if that makes sense
			var jsname = node.getAttribute("jsId");
			if(jsname){
				d.setObject(jsname, instance);
			}

			// process connections and startup functions
			if(!ps){
				d.forEach(connects, function(connect){
					d.connect(instance, connect.event, null, connect.func);
				});
				d.forEach(calls, function(func){
					func.call(instance);
				});
			}
		});

		// Call startup on each top level instance if it makes sense (as for
		// widgets).  Parent widgets will recursively call startup on their
		// (non-top level) children
		d.forEach(thelist, function(instance){
			if(	instance  && 
				instance.startup &&
				!instance._started && 
				(!instance.getParent || !instance.getParent())
			){
				instance.startup();
			}
		});
		return thelist;
	};

	this.parse = function(/*DomNode?*/ rootNode){
		// summary:
		//		Search specified node (or root node) recursively for class instances,
		//		and instantiate them Searches for
		//		dojoType="qualified.class.name"
		var list = d.query(qry, rootNode);
		// go build the object instances
		var instances = this.instantiate(list);
		return instances;
	};
}();

//Register the parser callback. It should be the first callback
//after the a11y test.

(function(){
	var parseRunner = function(){ 
		if(dojo.config["parseOnLoad"] == true){
			dojo.parser.parse(); 
		}
	};

	// FIXME: need to clobber cross-dependency!!
	if(dojo.exists("dijit.wai.onload") && (dijit.wai.onload === dojo._loaders[0])){
		dojo._loaders.splice(1, 0, parseRunner);
	}else{
		dojo._loaders.unshift(parseRunner);
	}
})();

//TODO: ported from 0.4.x Dojo.  Can we reduce this?
dojo.parser._anonCtr = 0;
dojo.parser._anon = {}; // why is this property required?
dojo.parser._nameAnonFunc = function(/*Function*/anonFuncPtr, /*Object*/thisObj){
	// summary:
	//		Creates a reference to anonFuncPtr in thisObj with a completely
	//		unique name. The new name is returned as a String. 
	var jpn = "$joinpoint";
	var nso = (thisObj|| dojo.parser._anon);
	if(dojo.isIE){
		var cn = anonFuncPtr["__dojoNameCache"];
		if(cn && nso[cn] === anonFuncPtr){
			return anonFuncPtr["__dojoNameCache"];
		}
	}
	var ret = "__"+dojo.parser._anonCtr++;
	while(typeof nso[ret] != "undefined"){
		ret = "__"+dojo.parser._anonCtr++;
	}
	nso[ret] = anonFuncPtr;
	return ret; // String
}

}

if(!dojo._hasResource["dijit._Templated"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._Templated"] = true;
dojo.provide("dijit._Templated");





dojo.declare("dijit._Templated",
	null,
	{
		//	summary:
		//		Mixin for widgets that are instantiated from a template
		// 
		// templateNode: DomNode
		//		a node that represents the widget template. Pre-empts both templateString and templatePath.
		templateNode: null,

		// templateString: String
		//		a string that represents the widget template. Pre-empts the
		//		templatePath. In builds that have their strings "interned", the
		//		templatePath is converted to an inline templateString, thereby
		//		preventing a synchronous network call.
		templateString: null,

		// templatePath: String
		//	Path to template (HTML file) for this widget relative to dojo.baseUrl
		templatePath: null,

		// widgetsInTemplate: Boolean
		//		should we parse the template to find widgets that might be
		//		declared in markup inside it? false by default.
		widgetsInTemplate: false,

		// containerNode: DomNode
		//		holds child elements. "containerNode" is generally set via a
		//		dojoAttachPoint assignment and it designates where children of
		//		the src dom node will be placed
		containerNode: null,

		// skipNodeCache: Boolean
		//		if using a cached widget template node poses issues for a
		//		particular widget class, it can set this property to ensure
		//		that its template is always re-built from a string
		_skipNodeCache: false,

		_stringRepl: function(tmpl){
			var className = this.declaredClass, _this = this;
			// Cache contains a string because we need to do property replacement
			// do the property replacement
			return dojo.string.substitute(tmpl, this, function(value, key){
				if(key.charAt(0) == '!'){ value = _this[key.substr(1)]; }
				if(typeof value == "undefined"){ throw new Error(className+" template:"+key); } // a debugging aide
				if(!value){ return ""; }

				// Substitution keys beginning with ! will skip the transform step,
				// in case a user wishes to insert unescaped markup, e.g. ${!foo}
				return key.charAt(0) == "!" ? value :
					// Safer substitution, see heading "Attribute values" in
					// http://www.w3.org/TR/REC-html40/appendix/notes.html#h-B.3.2
					value.toString().replace(/"/g,"&quot;"); //TODO: add &amp? use encodeXML method?
			}, this);
		},

		// method over-ride
		buildRendering: function(){
			// summary:
			//		Construct the UI for this widget from a template, setting this.domNode.

			// Lookup cached version of template, and download to cache if it
			// isn't there already.  Returns either a DomNode or a string, depending on
			// whether or not the template contains ${foo} replacement parameters.
			var cached = dijit._Templated.getCachedTemplate(this.templatePath, this.templateString, this._skipNodeCache);

			var node;
			if(dojo.isString(cached)){
				node = dijit._Templated._createNodesFromText(this._stringRepl(cached))[0];
			}else{
				// if it's a node, all we have to do is clone it
				node = cached.cloneNode(true);
			}

			// recurse through the node, looking for, and attaching to, our
			// attachment points which should be defined on the template node.
			this._attachTemplateNodes(node);

			var source = this.srcNodeRef;
			if(source && source.parentNode){
				source.parentNode.replaceChild(node, source);
			}

			this.domNode = node;
			if(this.widgetsInTemplate){
				var cw = this._supportingWidgets  = dojo.parser.parse(node);
				this._attachTemplateNodes(cw, function(n,p){
					return n[p];
				});
			}

			this._fillContent(source);
		},

		_fillContent: function(/*DomNode*/ source){
			// summary:
			//		relocate source contents to templated container node
			//		this.containerNode must be able to receive children, or exceptions will be thrown
			var dest = this.containerNode;
			if(source && dest){
				while(source.hasChildNodes()){
					dest.appendChild(source.firstChild);
				}
			}
		},

		_attachTemplateNodes: function(rootNode, getAttrFunc){
			// summary: Iterate through the template and attach functions and nodes accordingly.	
			// description:		
			//		Map widget properties and functions to the handlers specified in
			//		the dom node and it's descendants. This function iterates over all
			//		nodes and looks for these properties:
			//			* dojoAttachPoint
			//			* dojoAttachEvent	
			//			* waiRole
			//			* waiState
			// rootNode: DomNode|Array[Widgets]
			//		the node to search for properties. All children will be searched.
			// getAttrFunc: function?
			//		a function which will be used to obtain property for a given
			//		DomNode/Widget

			getAttrFunc = getAttrFunc || function(n,p){ return n.getAttribute(p); };

			var nodes = dojo.isArray(rootNode) ? rootNode : (rootNode.all || rootNode.getElementsByTagName("*"));
			var x=dojo.isArray(rootNode)?0:-1;
			for(; x<nodes.length; x++){
				var baseNode = (x == -1) ? rootNode : nodes[x];
				if(this.widgetsInTemplate && getAttrFunc(baseNode,'dojoType')){
					continue;
				}
				// Process dojoAttachPoint
				var attachPoint = getAttrFunc(baseNode, "dojoAttachPoint");
				if(attachPoint){
					var point, points = attachPoint.split(/\s*,\s*/);
					while((point = points.shift())){
						if(dojo.isArray(this[point])){
							this[point].push(baseNode);
						}else{
							this[point]=baseNode;
						}
					}
				}

				// Process dojoAttachEvent
				var attachEvent = getAttrFunc(baseNode, "dojoAttachEvent");
				if(attachEvent){
					// NOTE: we want to support attributes that have the form
					// "domEvent: nativeEvent; ..."
					var event, events = attachEvent.split(/\s*,\s*/);
					var trim = dojo.trim;
					while((event = events.shift())){
						if(event){
							var thisFunc = null;
							if(event.indexOf(":") != -1){
								// oh, if only JS had tuple assignment
								var funcNameArr = event.split(":");
								event = trim(funcNameArr[0]);
								thisFunc = trim(funcNameArr[1]);
							}else{
								event = trim(event);
							}
							if(!thisFunc){
								thisFunc = event;
							}
							this.connect(baseNode, event, thisFunc);
						}
					}
				}

				// waiRole, waiState
				var role = getAttrFunc(baseNode, "waiRole");
				if(role){
					dijit.setWaiRole(baseNode, role);
				}
				var values = getAttrFunc(baseNode, "waiState");
				if(values){
					dojo.forEach(values.split(/\s*,\s*/), function(stateValue){
						if(stateValue.indexOf('-') != -1){
							var pair = stateValue.split('-');
							dijit.setWaiState(baseNode, pair[0], pair[1]);
						}
					});
				}

			}
		}
	}
);

// key is either templatePath or templateString; object is either string or DOM tree
dijit._Templated._templateCache = {};

dijit._Templated.getCachedTemplate = function(templatePath, templateString, alwaysUseString){
	// summary:
	//		Static method to get a template based on the templatePath or
	//		templateString key
	// templatePath: String
	//		The URL to get the template from. dojo.uri.Uri is often passed as well.
	// templateString: String?
	//		a string to use in lieu of fetching the template from a URL. Takes precedence
	//		over templatePath
	// Returns: Mixed
	//	Either string (if there are ${} variables that need to be replaced) or just
	//	a DOM tree (if the node can be cloned directly)

	// is it already cached?
	var tmplts = dijit._Templated._templateCache;
	var key = templateString || templatePath;
	var cached = tmplts[key];
	if(cached){
		return cached;
	}

	// If necessary, load template string from template path
	if(!templateString){
		templateString = dijit._Templated._sanitizeTemplateString(dojo._getText(templatePath));
	}

	templateString = dojo.string.trim(templateString);

	if(alwaysUseString || templateString.match(/\$\{([^\}]+)\}/g)){
		// there are variables in the template so all we can do is cache the string
		return (tmplts[key] = templateString); //String
	}else{
		// there are no variables in the template so we can cache the DOM tree
		return (tmplts[key] = dijit._Templated._createNodesFromText(templateString)[0]); //Node
	}
};

dijit._Templated._sanitizeTemplateString = function(/*String*/tString){
	// summary: 
	//		Strips <?xml ...?> declarations so that external SVG and XML
	// 		documents can be added to a document without worry. Also, if the string
	//		is an HTML document, only the part inside the body tag is returned.
	if(tString){
		tString = tString.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im, "");
		var matches = tString.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
		if(matches){
			tString = matches[1];
		}
	}else{
		tString = "";
	}
	return tString; //String
};


if(dojo.isIE){
	dojo.addOnUnload(function(){
		var cache = dijit._Templated._templateCache;
		for(var key in cache){
			var value = cache[key];
			if(!isNaN(value.nodeType)){ // isNode equivalent
				dojo._destroyElement(value);
			}
			delete cache[key];
		}
	});
}

(function(){
	var tagMap = {
		cell: {re: /^<t[dh][\s\r\n>]/i, pre: "<table><tbody><tr>", post: "</tr></tbody></table>"},
		row: {re: /^<tr[\s\r\n>]/i, pre: "<table><tbody>", post: "</tbody></table>"},
		section: {re: /^<(thead|tbody|tfoot)[\s\r\n>]/i, pre: "<table>", post: "</table>"}
	};

	// dummy container node used temporarily to hold nodes being created
	var tn;

	dijit._Templated._createNodesFromText = function(/*String*/text){
		// summary:
		//	Attempts to create a set of nodes based on the structure of the passed text.

		if(!tn){
			tn = dojo.doc.createElement("div");
			tn.style.display="none";
			dojo.body().appendChild(tn);
		}
		var tableType = "none";
		var rtext = text.replace(/^\s+/, "");
		for(var type in tagMap){
			var map = tagMap[type];
			if(map.re.test(rtext)){
				tableType = type;
				text = map.pre + text + map.post;
				break;
			}
		}

		tn.innerHTML = text;
		if(tn.normalize){
			tn.normalize();
		}

		var tag = { cell: "tr", row: "tbody", section: "table" }[tableType];
		var _parent = (typeof tag != "undefined") ?
						tn.getElementsByTagName(tag)[0] :
						tn;

		var nodes = [];
		while(_parent.firstChild){
			nodes.push(_parent.removeChild(_parent.firstChild));
		}
		tn.innerHTML="";
		return nodes;	//	Array
	}
})();

// These arguments can be specified for widgets which are used in templates.
// Since any widget can be specified as sub widgets in template, mix it
// into the base widget class.  (This is a hack, but it's effective.)
dojo.extend(dijit._Widget,{
	dojoAttachEvent: "",
	dojoAttachPoint: "",
	waiRole: "",
	waiState:""
})

}

if(!dojo._hasResource["dijit._Container"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit._Container"] = true;
dojo.provide("dijit._Container");

dojo.declare("dijit._Contained",
	null,
	{
		// summary
		//		Mixin for widgets that are children of a container widget
		//
		// example:
		// | 	// make a basic custom widget that knows about it's parents
		// |	dojo.declare("my.customClass",[dijit._Widget,dijit._Contained],{});
		// 
		getParent: function(){
			// summary:
			//		Returns the parent widget of this widget, assuming the parent
			//		implements dijit._Container
			for(var p=this.domNode.parentNode; p; p=p.parentNode){
				var id = p.getAttribute && p.getAttribute("widgetId");
				if(id){
					var parent = dijit.byId(id);
					return parent.isContainer ? parent : null;
				}
			}
			return null;
		},

		_getSibling: function(which){
			var node = this.domNode;
			do{
				node = node[which+"Sibling"];
			}while(node && node.nodeType != 1);
			if(!node){ return null; } // null
			var id = node.getAttribute("widgetId");
			return dijit.byId(id);
		},

		getPreviousSibling: function(){
			// summary:
			//		Returns null if this is the first child of the parent,
			//		otherwise returns the next element sibling to the "left".

			return this._getSibling("previous"); // Mixed
		},

		getNextSibling: function(){
			// summary:
			//		Returns null if this is the last child of the parent,
			//		otherwise returns the next element sibling to the "right".

			return this._getSibling("next"); // Mixed
		}
	}
);

dojo.declare("dijit._Container",
	null,
	{
		// summary:
		//		Mixin for widgets that contain a list of children.
		// description:
		//		Use this mixin when the widget needs to know about and
		//		keep track of it's widget children. Widgets like SplitContainer
		//		and TabContainer.  

		isContainer: true,

		addChild: function(/*Widget*/ widget, /*int?*/ insertIndex){
			// summary:
			//		Process the given child widget, inserting it's dom node as
			//		a child of our dom node

			if(insertIndex === undefined){
				insertIndex = "last";
			}
			var refNode = this.containerNode || this.domNode;
			if(insertIndex && typeof insertIndex == "number"){
				var children = dojo.query("> [widgetid]", refNode);
				if(children && children.length >= insertIndex){
					refNode = children[insertIndex-1]; insertIndex = "after";
				}
			}
			dojo.place(widget.domNode, refNode, insertIndex);

			// If I've been started but the child widget hasn't been started,
			// start it now.  Make sure to do this after widget has been
			// inserted into the DOM tree, so it can see that it's being controlled by me,
			// so it doesn't try to size itself.
			if(this._started && !widget._started){
				widget.startup();
			}
		},

		removeChild: function(/*Widget*/ widget){
			// summary:
			//		Removes the passed widget instance from this widget but does
			//		not destroy it
			var node = widget.domNode;
			node.parentNode.removeChild(node);	// detach but don't destroy
		},

		_nextElement: function(node){
			do{
				node = node.nextSibling;
			}while(node && node.nodeType != 1);
			return node;
		},

		_firstElement: function(node){
			node = node.firstChild;
			if(node && node.nodeType != 1){
				node = this._nextElement(node);
			}
			return node;
		},

		getChildren: function(){
			// summary:
			//		Returns array of children widgets
			return dojo.query("> [widgetId]", this.containerNode || this.domNode).map(dijit.byNode); // Array
		},

		hasChildren: function(){
			// summary:
			//		Returns true if widget has children
			var cn = this.containerNode || this.domNode;
			return !!this._firstElement(cn); // Boolean
		},

		_getSiblingOfChild: function(/*Widget*/ child, /*int*/ dir){
			// summary:
			//		Get the next or previous widget sibling of child
			// dir:
			//		if 1, get the next sibling
			//		if -1, get the previous sibling
			var node = child.domNode;
			var which = (dir>0 ? "nextSibling" : "previousSibling");
			do{
				node = node[which];
			}while(node && (node.nodeType != 1 || !dijit.byNode(node)));
			return node ? dijit.byNode(node) : null;
		}
	}
);

dojo.declare("dijit._KeyNavContainer",
	[dijit._Container],
	{

		// summary: A _Container with keyboard navigation of its children.
		// decscription:
		//		To use this mixin, call connectKeyNavHandlers() in
		//		postCreate() and call startupKeyNavChildren() in startup().
		//		It provides normalized keyboard and focusing code for Container
		//		widgets.
/*=====
		// focusedChild: Widget
		//		The currently focused child widget, or null if there isn't one
		focusedChild: null,
=====*/

		_keyNavCodes: {},

		connectKeyNavHandlers: function(/*Array*/ prevKeyCodes, /*Array*/ nextKeyCodes){
			// summary:
			//		Call in postCreate() to attach the keyboard handlers
			//		to the container.
			// preKeyCodes: Array
			//		Key codes for navigating to the previous child.
			// nextKeyCodes: Array
			//		Key codes for navigating to the next child.

			var keyCodes = this._keyNavCodes = {};
			var prev = dojo.hitch(this, this.focusPrev);
			var next = dojo.hitch(this, this.focusNext);
			dojo.forEach(prevKeyCodes, function(code){ keyCodes[code] = prev });
			dojo.forEach(nextKeyCodes, function(code){ keyCodes[code] = next });
			this.connect(this.domNode, "onkeypress", "_onContainerKeypress");
			this.connect(this.domNode, "onfocus", "_onContainerFocus");
		},

		startupKeyNavChildren: function(){
			// summary:
			//		Call in startup() to set child tabindexes to -1
			dojo.forEach(this.getChildren(), dojo.hitch(this, "_startupChild"));
		},

		addChild: function(/*Widget*/ widget, /*int?*/ insertIndex){
			// summary: Add a child to our _Container
			dijit._KeyNavContainer.superclass.addChild.apply(this, arguments);
			this._startupChild(widget);
		},

		focus: function(){
			// summary: Default focus() implementation: focus the first child.
			this.focusFirstChild();
		},

		focusFirstChild: function(){
			// summary: Focus the first focusable child in the container.
			this.focusChild(this._getFirstFocusableChild());
		},

		focusNext: function(){
			// summary: Focus the next widget or focal node (for widgets
			//		with multiple focal nodes) within this container.
			if(this.focusedChild && this.focusedChild.hasNextFocalNode
					&& this.focusedChild.hasNextFocalNode()){
				this.focusedChild.focusNext();
				return;
			}
			var child = this._getNextFocusableChild(this.focusedChild, 1);
			if(child.getFocalNodes){
				this.focusChild(child, child.getFocalNodes()[0]);
			}else{
				this.focusChild(child);
			}
		},

		focusPrev: function(){
			// summary: Focus the previous widget or focal node (for widgets
			//		with multiple focal nodes) within this container.
			if(this.focusedChild && this.focusedChild.hasPrevFocalNode
					&& this.focusedChild.hasPrevFocalNode()){
				this.focusedChild.focusPrev();
				return;
			}
			var child = this._getNextFocusableChild(this.focusedChild, -1);
			if(child.getFocalNodes){
				var nodes = child.getFocalNodes();
				this.focusChild(child, nodes[nodes.length-1]);
			}else{
				this.focusChild(child);
			}
		},

		focusChild: function(/*Widget*/ widget, /*Node?*/ node){
			// summary: Focus widget. Optionally focus 'node' within widget.
			if(widget){
				if(this.focusedChild && widget !== this.focusedChild){
					this._onChildBlur(this.focusedChild);
				}
				this.focusedChild = widget;
				if(node && widget.focusFocalNode){
					widget.focusFocalNode(node);
				}else{
					widget.focus();
				}
			}
		},

		_startupChild: function(/*Widget*/ widget){
			// summary:
			//		Set tabindex="-1" on focusable widgets so that we
			// 		can focus them programmatically and by clicking.
			//		Connect focus and blur handlers.
			if(widget.getFocalNodes){
				dojo.forEach(widget.getFocalNodes(), function(node){
					dojo.attr(node, "tabindex", -1);
					this._connectNode(node);
				}, this);
			}else{
				var node = widget.focusNode || widget.domNode;
				if(widget.isFocusable()){
					dojo.attr(node, "tabindex", -1);
				}
				this._connectNode(node);
			}
		},

		_connectNode: function(/*Element*/ node){
			this.connect(node, "onfocus", "_onNodeFocus");
			this.connect(node, "onblur", "_onNodeBlur");
		},

		_onContainerFocus: function(evt){
			// focus bubbles on Firefox,
			// so just make sure that focus has really gone to the container
			if(evt.target === this.domNode){
				this.focusFirstChild();
			}
		},

		_onContainerKeypress: function(evt){
			if(evt.ctrlKey || evt.altKey){ return; }
			var func = this._keyNavCodes[evt.keyCode];
			if(func){
				func();
				dojo.stopEvent(evt);
			}
		},

		_onNodeFocus: function(evt){
			// while focus is on a child,
			// take the container out of the tab order so that
			// we can shift-tab to the element before the container
			dojo.attr(this.domNode, "tabindex", -1);
			// record the child that has been focused
			var widget = dijit.getEnclosingWidget(evt.target);
			if(widget && widget.isFocusable()){
				this.focusedChild = widget;
			}
			dojo.stopEvent(evt);
		},

		_onNodeBlur: function(evt){
			// when focus leaves a child,
			// reinstate the container's tabindex
			if(this.tabIndex){
				dojo.attr(this.domNode, "tabindex", this.tabIndex);
			}
			dojo.stopEvent(evt);
		},

		_onChildBlur: function(/*Widget*/ widget){
			// summary:
			//		Called when focus leaves a child widget to go
			//		to a sibling widget.
		},

		_getFirstFocusableChild: function(){
			return this._getNextFocusableChild(null, 1);
		},

		_getNextFocusableChild: function(child, dir){
			if(child){
				child = this._getSiblingOfChild(child, dir);
			}
			var children = this.getChildren();
			for(var i=0; i < children.length; i++){
				if(!child){
					child = children[(dir>0) ? 0 : (children.length-1)];
				}
				if(child.isFocusable()){
					return child;
				}
				child = this._getSiblingOfChild(child, dir);
			}
			// no focusable child found
			return null;
		}
	}
);

}

if(!dojo._hasResource["dojo.regexp"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.regexp"] = true;
dojo.provide("dojo.regexp");

/*=====
dojo.regexp = {
	// summary: Regular expressions and Builder resources
};
=====*/

dojo.regexp.escapeString = function(/*String*/str, /*String?*/except){
	//	summary:
	//		Adds escape sequences for special characters in regular expressions
	// except:
	//		a String with special characters to be left unescaped

//	return str.replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm, "\\$1"); // string
	return str.replace(/([\.$?*!=:|{}\(\)\[\]\\\/^])/g, function(ch){
		if(except && except.indexOf(ch) != -1){
			return ch;
		}
		return "\\" + ch;
	}); // String
}

dojo.regexp.buildGroupRE = function(/*Object|Array*/arr, /*Function*/re, /*Boolean?*/nonCapture){
	//	summary:
	//		Builds a regular expression that groups subexpressions
	//	description:
	//		A utility function used by some of the RE generators. The
	//		subexpressions are constructed by the function, re, in the second
	//		parameter.  re builds one subexpression for each elem in the array
	//		a, in the first parameter. Returns a string for a regular
	//		expression that groups all the subexpressions.
	// arr:
	//		A single value or an array of values.
	// re:
	//		A function. Takes one parameter and converts it to a regular
	//		expression. 
	// nonCapture:
	//		If true, uses non-capturing match, otherwise matches are retained
	//		by regular expression. Defaults to false

	// case 1: a is a single value.
	if(!(arr instanceof Array)){
		return re(arr); // String
	}

	// case 2: a is an array
	var b = [];
	for(var i = 0; i < arr.length; i++){
		// convert each elem to a RE
		b.push(re(arr[i]));
	}

	 // join the REs as alternatives in a RE group.
	return dojo.regexp.group(b.join("|"), nonCapture); // String
}

dojo.regexp.group = function(/*String*/expression, /*Boolean?*/nonCapture){
	// summary:
	//		adds group match to expression
	// nonCapture:
	//		If true, uses non-capturing match, otherwise matches are retained
	//		by regular expression. 
	return "(" + (nonCapture ? "?:":"") + expression + ")"; // String
}

}

if(!dojo._hasResource["dojo.cookie"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.cookie"] = true;
dojo.provide("dojo.cookie");



/*=====
dojo.__cookieProps = function(){
	//	expires: Date|String|Number?
	//		If a number, the number of days from today at which the cookie
	//		will expire. If a date, the date past which the cookie will expire.
	//		If expires is in the past, the cookie will be deleted.
	//		If expires is omitted or is 0, the cookie will expire when the browser closes. << FIXME: 0 seems to disappear right away? FF3.
	//	path: String?
	//		The path to use for the cookie.
	//	domain: String?
	//		The domain to use for the cookie.
	//	secure: Boolean?
	//		Whether to only send the cookie on secure connections
	this.expires = expires;
	this.path = path;
	this.domain = domain;
	this.secure = secure;
}
=====*/


dojo.cookie = function(/*String*/name, /*String?*/value, /*dojo.__cookieProps?*/props){
	//	summary: 
	//		Get or set a cookie.
	//	description:
	// 		If one argument is passed, returns the value of the cookie
	// 		For two or more arguments, acts as a setter.
	//	name:
	//		Name of the cookie
	//	value:
	//		Value for the cookie
	//	props: 
	//		Properties for the cookie
	//	example:
	//		set a cookie with the JSON-serialized contents of an object which
	//		will expire 5 days from now:
	//	|	dojo.cookie("configObj", dojo.toJson(config), { expires: 5 });
	//	
	//	example:
	//		de-serialize a cookie back into a JavaScript object:
	//	|	var config = dojo.fromJson(dojo.cookie("configObj"));
	//	
	//	example:
	//		delete a cookie:
	//	|	dojo.cookie("configObj", null, {expires: -1});
	var c = document.cookie;
	if(arguments.length == 1){
		var matches = c.match(new RegExp("(?:^|; )" + dojo.regexp.escapeString(name) + "=([^;]*)"));
		return matches ? decodeURIComponent(matches[1]) : undefined; // String or undefined
	}else{
		props = props || {};
// FIXME: expires=0 seems to disappear right away, not on close? (FF3)  Change docs?
		var exp = props.expires;
		if(typeof exp == "number"){ 
			var d = new Date();
			d.setTime(d.getTime() + exp*24*60*60*1000);
			exp = props.expires = d;
		}
		if(exp && exp.toUTCString){ props.expires = exp.toUTCString(); }

		value = encodeURIComponent(value);
		var updatedCookie = name + "=" + value;
		for(propName in props){
			updatedCookie += "; " + propName;
			var propValue = props[propName];
			if(propValue !== true){ updatedCookie += "=" + propValue; }
		}
		document.cookie = updatedCookie;
	}
};

dojo.cookie.isSupported = function(){
	//	summary:
	//		Use to determine if the current browser supports cookies or not.
	//		
	//		Returns true if user allows cookies.
	//		Returns false if user doesn't allow cookies.

	if(!("cookieEnabled" in navigator)){
		this("__djCookieTest__", "CookiesAllowed");
		navigator.cookieEnabled = this("__djCookieTest__") == "CookiesAllowed";
		if(navigator.cookieEnabled){
			this("__djCookieTest__", "", {expires: -1});
		}
	}
	return navigator.cookieEnabled;
};

}

if(!dojo._hasResource["dijit.Tree"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dijit.Tree"] = true;
dojo.provide("dijit.Tree");








dojo.declare(
	"dijit._TreeNode",
	[dijit._Widget, dijit._Templated, dijit._Container, dijit._Contained],
{
	// summary
	//		Single node within a tree

	// item: dojo.data.Item
	//		the dojo.data entry this tree represents
	item: null,	

	isTreeNode: true,

	// label: String
	//		Text of this tree node
	label: "",
	
	isExpandable: null, // show expando node
	
	isExpanded: false,

	// state: String
	//		dynamic loading-related stuff.
	//		When an empty folder node appears, it is "UNCHECKED" first,
	//		then after dojo.data query it becomes "LOADING" and, finally "LOADED"	
	state: "UNCHECKED",
	
	templateString:"<div class=\"dijitTreeNode\" waiRole=\"presentation\"\r\n\t><div dojoAttachPoint=\"rowNode\" waiRole=\"presentation\"\r\n\t\t><span dojoAttachPoint=\"expandoNode\" class=\"dijitTreeExpando\" waiRole=\"presentation\"\r\n\t\t></span\r\n\t\t><span dojoAttachPoint=\"expandoNodeText\" class=\"dijitExpandoText\" waiRole=\"presentation\"\r\n\t\t></span\r\n\t\t><div dojoAttachPoint=\"contentNode\" class=\"dijitTreeContent\" waiRole=\"presentation\">\r\n\t\t\t<div dojoAttachPoint=\"iconNode\" class=\"dijitInline dijitTreeIcon\" waiRole=\"presentation\"></div>\r\n\t\t\t<span dojoAttachPoint=\"labelNode\" class=\"dijitTreeLabel\" wairole=\"treeitem\" tabindex=\"-1\" waiState=\"selected-false\" dojoAttachEvent=\"onfocus:_onNodeFocus\"></span>\r\n\t\t</div\r\n\t></div>\r\n</div>\r\n",		

	postCreate: function(){
		// set label, escaping special characters
		this.setLabelNode(this.label);

		// set expand icon for leaf
		this._setExpando();

		// set icon and label class based on item
		this._updateItemClasses(this.item);

		if(this.isExpandable){
			dijit.setWaiState(this.labelNode, "expanded", this.isExpanded);
		}
	},

	markProcessing: function(){
		// summary: visually denote that tree is loading data, etc.
		this.state = "LOADING";
		this._setExpando(true);	
	},

	unmarkProcessing: function(){
		// summary: clear markup from markProcessing() call
		this._setExpando(false);	
	},

	_updateItemClasses: function(item){
		// summary: set appropriate CSS classes for icon and label dom node (used to allow for item updates to change respective CSS)
		var tree = this.tree, model = tree.model;
		if(tree._v10Compat && item === model.root){
			// For back-compat with 1.0, need to use null to specify root item (TODO: remove in 2.0)
			item = null;
		}
		this.iconNode.className = "dijitInline dijitTreeIcon " + tree.getIconClass(item, this.isExpanded);
		this.labelNode.className = "dijitTreeLabel " + tree.getLabelClass(item, this.isExpanded);
	},

	_updateLayout: function(){
		// summary: set appropriate CSS classes for this.domNode
		var parent = this.getParent();
		if(!parent || parent.rowNode.style.display == "none"){
			/* if we are hiding the root node then make every first level child look like a root node */
			dojo.addClass(this.domNode, "dijitTreeIsRoot");
		}else{
			dojo.toggleClass(this.domNode, "dijitTreeIsLast", !this.getNextSibling());
		}
	},

	_setExpando: function(/*Boolean*/ processing){
		// summary: set the right image for the expando node

		// apply the appropriate class to the expando node
		var styles = ["dijitTreeExpandoLoading", "dijitTreeExpandoOpened",
			"dijitTreeExpandoClosed", "dijitTreeExpandoLeaf"];
		var idx = processing ? 0 : (this.isExpandable ?	(this.isExpanded ? 1 : 2) : 3);
		dojo.forEach(styles,
			function(s){
				dojo.removeClass(this.expandoNode, s);
			}, this
		);
		dojo.addClass(this.expandoNode, styles[idx]);

		// provide a non-image based indicator for images-off mode
		this.expandoNodeText.innerHTML =
			processing ? "*" :
				(this.isExpandable ?
					(this.isExpanded ? "-" : "+") : "*");
	},	

	expand: function(){
		// summary: show my children
		if(this.isExpanded){ return; }
		// cancel in progress collapse operation
		if(this._wipeOut.status() == "playing"){
			this._wipeOut.stop();
		}

		this.isExpanded = true;
		dijit.setWaiState(this.labelNode, "expanded", "true");
		dijit.setWaiRole(this.containerNode, "group");
		this.contentNode.className = "dijitTreeContent dijitTreeContentExpanded";
		this._setExpando();
		this._updateItemClasses(this.item);

		this._wipeIn.play();
	},

	collapse: function(){					
		if(!this.isExpanded){ return; }

		// cancel in progress expand operation
		if(this._wipeIn.status() == "playing"){
			this._wipeIn.stop();
		}

		this.isExpanded = false;
		dijit.setWaiState(this.labelNode, "expanded", "false");
		this.contentNode.className = "dijitTreeContent";
		this._setExpando();
		this._updateItemClasses(this.item);

		this._wipeOut.play();
	},

	setLabelNode: function(label){
		this.labelNode.innerHTML="";
		this.labelNode.appendChild(dojo.doc.createTextNode(label));
	},

	setChildItems: function(/* Object[] */ items){
		// summary:
		//		Sets the child items of this node, removing/adding nodes
		//		from current children to match specified items[] array.

		var tree = this.tree,
			model = tree.model;

		// Orphan all my existing children.
		// If items contains some of the same items as before then we will reattach them.
		// Don't call this.removeChild() because that will collapse the tree etc.
		this.getChildren().forEach(function(child){
			dijit._Container.prototype.removeChild.call(this, child);
		}, this);

		this.state = "LOADED";

		if(items && items.length > 0){
			this.isExpandable = true;
			if(!this.containerNode){ // maybe this node was unfolderized and still has container
				this.containerNode = this.tree.containerNodeTemplate.cloneNode(true);
				this.domNode.appendChild(this.containerNode);
			}

			// Create _TreeNode widget for each specified tree node, unless one already
			// exists and isn't being used (presumably it's from a DnD move and was recently
			// released
			dojo.forEach(items, function(item){
				var id = model.getIdentity(item),
					existingNode = tree._itemNodeMap[id],
					node = 
						( existingNode && !existingNode.getParent() ) ?
						existingNode :
						new dijit._TreeNode({
							item: item,
							tree: tree,
							isExpandable: model.mayHaveChildren(item),
							label: tree.getLabel(item)
						});
				this.addChild(node);
				// note: this won't work if there are two nodes for one item (multi-parented items); will be fixed later
				tree._itemNodeMap[id] = node;
				if(this.tree.persist){
					if(tree._openedItemIds[id]){
						tree._expandNode(node);
					}
				}
			}, this);

			// note that updateLayout() needs to be called on each child after
			// _all_ the children exist
			dojo.forEach(this.getChildren(), function(child, idx){
				child._updateLayout();
			});
		}else{
			this.isExpandable=false;
		}

		if(this._setExpando){
			// change expando to/from dot or + icon, as appropriate
			this._setExpando(false);
		}

		// On initial tree show, put focus on either the root node of the tree,
		// or the first child, if the root node is hidden
		if(!this.parent){
			var fc = this.tree.showRoot ? this : this.getChildren()[0],
				tabnode = fc ? fc.labelNode : this.domNode;
			tabnode.setAttribute("tabIndex", "0");
		}

		// create animations for showing/hiding the children (if children exist)
		if(this.containerNode && !this._wipeIn){
			this._wipeIn = dojo.fx.wipeIn({node: this.containerNode, duration: 150});
			this._wipeOut = dojo.fx.wipeOut({node: this.containerNode, duration: 150});
		}
	},

	removeChild: function(/* treeNode */ node){
		this.inherited(arguments);

		var children = this.getChildren();		
		if(children.length == 0){
			this.isExpandable = false;
			this.collapse();
		}

		dojo.forEach(children, function(child){
				child._updateLayout();
		});
	},

	makeExpandable: function(){
		//summary
		//		if this node wasn't already showing the expando node,
		//		turn it into one and call _setExpando()
		this.isExpandable = true;
		this._setExpando(false);
	},

	_onNodeFocus: function(evt){
		var node = dijit.getEnclosingWidget(evt.target);
		this.tree._onTreeFocus(node);
	}
});

dojo.declare(
	"dijit.Tree",
	[dijit._Widget, dijit._Templated],
{
	// summary
	//	This widget displays hierarchical data from a store.  A query is specified
	//	to get the "top level children" from a data store, and then those items are
	//	queried for their children and so on (but lazily, as the user clicks the expand node).
	//
	//	Thus in the default mode of operation this widget is technically a forest, not a tree,
	//	in that there can be multiple "top level children".  However, if you specify label,
	//	then a special top level node (not corresponding to any item in the datastore) is
	//	created, to father all the top level children.

	// store: String||dojo.data.Store
	//	The store to get data to display in the tree.
	//	May remove for 2.0 in favor of "model".
	store: null,

	// model: dijit.Tree.model
	//	Alternate interface from store to access data (and changes to data) in the tree
	model: null,

	// query: anything
	//	Specifies datastore query to return the root item for the tree.
	//
	//	Deprecated functionality: if the query returns multiple items, the tree is given
	//	a fake root node (not corresponding to any item in the data store), 
	//	whose children are the items that match this query.
	//
	//	The root node is shown or hidden based on whether a label is specified.
	//
	//	Having a query return multiple items is deprecated.
	//	If your store doesn't have a root item, wrap the store with
	//	dijit.tree.ForestStoreModel, and specify model=myModel
	//
	// example:
	//		{type:'continent'}
	query: null,

	// label: String
	//	Deprecated.  Use dijit.tree.ForestStoreModel directly instead.
	//	Used in conjunction with query parameter.
	//	If a query is specified (rather than a root node id), and a label is also specified,
	//	then a fake root node is created and displayed, with this label.
	label: "",

	// showRoot: Boolean
	//	Should the root node be displayed, or hidden?
	showRoot: true,

	// childrenAttr: String[]
	//		one ore more attributes that holds children of a tree node
	childrenAttr: ["children"],

	// openOnClick: Boolean
	//		If true, clicking a folder node's label will open it, rather than calling onClick()
	openOnClick: false,

	templateString:"<div class=\"dijitTreeContainer\" waiRole=\"tree\"\r\n\tdojoAttachEvent=\"onclick:_onClick,onkeypress:_onKeyPress\">\r\n</div>\r\n",		

	isExpandable: true,

	isTree: true,

	// persist: Boolean
	//	enables/disables use of cookies for state saving.
	persist: true,
	
	// dndController: String
	//	class name to use as as the dnd controller
	dndController: null,

	//parameters to pull off of the tree and pass on to the dndController as its params
	dndParams: ["onDndDrop","itemCreator","onDndCancel","checkAcceptance", "checkItemAcceptance"],

	//declare the above items so they can be pulled from the tree's markup
	onDndDrop:null,
	itemCreator:null,
	onDndCancel:null,
	checkAcceptance:null,	
	checkItemAcceptance:null,

	_publish: function(/*String*/ topicName, /*Object*/ message){
		// summary:
		//		Publish a message for this widget/topic
		dojo.publish(this.id, [dojo.mixin({tree: this, event: topicName}, message||{})]);
	},

	postMixInProperties: function(){
		this.tree = this;

		this._itemNodeMap={};

		if(!this.cookieName){
			this.cookieName = this.id + "SaveStateCookie";
		}
	},

	postCreate: function(){
		// load in which nodes should be opened automatically
		if(this.persist){
			var cookie = dojo.cookie(this.cookieName);
			this._openedItemIds = {};
			if(cookie){
				dojo.forEach(cookie.split(','), function(item){
					this._openedItemIds[item] = true;
				}, this);
			}
		}
		
		// make template for container node (we will clone this and insert it into
		// any nodes that have children)
		var div = dojo.doc.createElement('div');
		div.style.display = 'none';
		div.className = "dijitTreeContainer";	
		dijit.setWaiRole(div, "presentation");
		this.containerNodeTemplate = div;

		// Create glue between store and Tree, if not specified directly by user
		if(!this.model){
			this._store2model();
		}

		// monitor changes to items
		this.connect(this.model, "onChange", "_onItemChange");
		this.connect(this.model, "onChildrenChange", "_onItemChildrenChange");
		// TODO: monitor item deletes so we don't end up w/orphaned nodes?

		this._load();

		this.inherited("postCreate", arguments);

		if(this.dndController){
			if(dojo.isString(this.dndController)){
				this.dndController= dojo.getObject(this.dndController);
			}	
			var params={};
			for (var i=0; i<this.dndParams.length;i++){
				if(this[this.dndParams[i]]){
					params[this.dndParams[i]]=this[this.dndParams[i]];
				}
			}
			this.dndController= new this.dndController(this, params);
		}
	},

	_store2model: function(){
		// summary: user specified a store&query rather than model, so create model from store/query
		this._v10Compat = true;
		dojo.deprecated("Tree: from version 2.0, should specify a model object rather than a store/query");

		var modelParams = {
			id: this.id + "_ForestStoreModel",
			store: this.store,
			query: this.query,
			childrenAttrs: this.childrenAttr
		};

		// Only override the model's mayHaveChildren() method if the user has specified an override
		if(this.params.mayHaveChildren){
			modelParams.mayHaveChildren = dojo.hitch(this, "mayHaveChildren");
		}
					
		if(this.params.getItemChildren){
			modelParams.getChildren = dojo.hitch(this, function(item, onComplete, onError){
				this.getItemChildren((this._v10Compat && item === this.model.root) ? null : item, onComplete, onError);
			});
		}
		this.model = new dijit.tree.ForestStoreModel(modelParams);
		
		// For backwards compatibility, the visibility of the root node is controlled by
		// whether or not the user has specified a label
		this.showRoot = Boolean(this.label);
	},

	_load: function(){
		// summary: initial load of the tree
		// load root node (possibly hidden) and it's children
		this.model.getRoot(
			dojo.hitch(this, function(item){
				var rn = this.rootNode = new dijit._TreeNode({
					item: item,
					tree: this,
					isExpandable: true,
					label: this.label || this.getLabel(item)
				});
				if(!this.showRoot){
					rn.rowNode.style.display="none";
				}
				this.domNode.appendChild(rn.domNode);
				this._itemNodeMap[this.model.getIdentity(item)] = rn;

				rn._updateLayout();		// sets "dijitTreeIsRoot" CSS classname

				// load top level children
				this._expandNode(rn);
			}),
			function(err){
				console.error(this, ": error loading root: ", err);
			}
		);
	},

	////////////// Data store related functions //////////////////////
	// These just get passed to the model; they are here for back-compat

	mayHaveChildren: function(/*dojo.data.Item*/ item){
		// summary
		//		User overridable function to tell if an item has or may have children.
		//		Controls whether or not +/- expando icon is shown.
		//		(For efficiency reasons we may not want to check if an element actually
		//		has children until user clicks the expando node)
	},

	getItemChildren: function(/*dojo.data.Item*/ parentItem, /*function(items)*/ onComplete){
		// summary
		// 		User overridable function that return array of child items of given parent item,
		//		or if parentItem==null then return top items in tree
	},

	///////////////////////////////////////////////////////
	// Functions for converting an item to a TreeNode
	getLabel: function(/*dojo.data.Item*/ item){
		// summary: user overridable function to get the label for a tree node (given the item)
		return this.model.getLabel(item);	// String
	},

	getIconClass: function(/*dojo.data.Item*/ item, /*Boolean*/ opened){
		// summary: user overridable function to return CSS class name to display icon
		return (!item || this.model.mayHaveChildren(item)) ? (opened ? "dijitFolderOpened" : "dijitFolderClosed") : "dijitLeaf"
	},

	getLabelClass: function(/*dojo.data.Item*/ item, /*Boolean*/ opened){
		// summary: user overridable function to return CSS class name to display label
	},

	/////////// Keyboard and Mouse handlers ////////////////////

	_onKeyPress: function(/*Event*/ e){
		// summary: translates keypress events into commands for the controller
		if(e.altKey){ return; }
		var treeNode = dijit.getEnclosingWidget(e.target);
		if(!treeNode){ return; }

		// Note: On IE e.keyCode is not 0 for printables so check e.charCode.
		// In dojo charCode is universally 0 for non-printables.
		if(e.charCode){  // handle printables (letter navigation)
			// Check for key navigation.
			var navKey = e.charCode;
			if(!e.altKey && !e.ctrlKey && !e.shiftKey && !e.metaKey){
				navKey = (String.fromCharCode(navKey)).toLowerCase();
				this._onLetterKeyNav( { node: treeNode, key: navKey } );
				dojo.stopEvent(e);
			}
		}else{  // handle non-printables (arrow keys)
			var map = this._keyHandlerMap;
			if(!map){
				// setup table mapping keys to events
				map = {};
				map[dojo.keys.ENTER]="_onEnterKey";
				map[dojo.keys.LEFT_ARROW]="_onLeftArrow";
				map[dojo.keys.RIGHT_ARROW]="_onRightArrow";
				map[dojo.keys.UP_ARROW]="_onUpArrow";
				map[dojo.keys.DOWN_ARROW]="_onDownArrow";
				map[dojo.keys.HOME]="_onHomeKey";
				map[dojo.keys.END]="_onEndKey";
				this._keyHandlerMap = map;
			}
			if(this._keyHandlerMap[e.keyCode]){
				this[this._keyHandlerMap[e.keyCode]]( { node: treeNode, item: treeNode.item } );	
				dojo.stopEvent(e);
			}
		}
	},

	_onEnterKey: function(/*Object*/ message){
		this._publish("execute", { item: message.item, node: message.node} );
		this.onClick(message.item, message.node);
	},

	_onDownArrow: function(/*Object*/ message){
		// summary: down arrow pressed; get next visible node, set focus there
		var node = this._getNextNode(message.node);
		if(node && node.isTreeNode){
			this.focusNode(node);
		}	
	},

	_onUpArrow: function(/*Object*/ message){
		// summary: up arrow pressed; move to previous visible node

		var node = message.node;

		// if younger siblings		
		var previousSibling = node.getPreviousSibling();
		if(previousSibling){
			node = previousSibling;
			// if the previous node is expanded, dive in deep
			while(node.isExpandable && node.isExpanded && node.hasChildren()){
				// move to the last child
				var children = node.getChildren();
				node = children[children.length-1];
			}
		}else{
			// if this is the first child, return the parent
			// unless the parent is the root of a tree with a hidden root
			var parent = node.getParent();
			if(!(!this.showRoot && parent === this.rootNode)){
				node = parent;
			}
		}

		if(node && node.isTreeNode){
			this.focusNode(node);
		}
	},

	_onRightArrow: function(/*Object*/ message){
		// summary: right arrow pressed; go to child node
		var node = message.node;

		// if not expanded, expand, else move to 1st child
		if(node.isExpandable && !node.isExpanded){
			this._expandNode(node);
		}else if(node.hasChildren()){
			node = node.getChildren()[0];
			if(node && node.isTreeNode){
				this.focusNode(node);
			}
		}
	},

	_onLeftArrow: function(/*Object*/ message){
		// summary:
		//		Left arrow pressed.
		//		If not collapsed, collapse, else move to parent.

		var node = message.node;

		if(node.isExpandable && node.isExpanded){
			this._collapseNode(node);
		}else{
			node = node.getParent();
			if(node && node.isTreeNode){
				this.focusNode(node);
			}
		}
	},

	_onHomeKey: function(){
		// summary: home pressed; get first visible node, set focus there
		var node = this._getRootOrFirstNode();
		if(node){
			this.focusNode(node);
		}
	},

	_onEndKey: function(/*Object*/ message){
		// summary: end pressed; go to last visible node

		var node = this;
		while(node.isExpanded){
			var c = node.getChildren();
			node = c[c.length - 1];
		}

		if(node && node.isTreeNode){
			this.focusNode(node);
		}
	},

	_onLetterKeyNav: function(message){
		// summary: letter key pressed; search for node starting with first char = key
		var node = startNode = message.node,
			key = message.key;
		do{
			node = this._getNextNode(node);
			//check for last node, jump to first node if necessary
			if(!node){
				node = this._getRootOrFirstNode();
			}
		}while(node !== startNode && (node.label.charAt(0).toLowerCase() != key));
		if(node && node.isTreeNode){
			// no need to set focus if back where we started
			if(node !== startNode){
				this.focusNode(node);
			}
		}
	},

	_onClick: function(/*Event*/ e){
		// summary: translates click events into commands for the controller to process
		var domElement = e.target;

		// find node
		var nodeWidget = dijit.getEnclosingWidget(domElement);	
		if(!nodeWidget || !nodeWidget.isTreeNode){
			return;
		}

		if( (this.openOnClick && nodeWidget.isExpandable) ||
			(domElement == nodeWidget.expandoNode || domElement == nodeWidget.expandoNodeText) ){
			// expando node was clicked, or label of a folder node was clicked; open it
			if(nodeWidget.isExpandable){
				this._onExpandoClick({node:nodeWidget});
			}
		}else{
			this._publish("execute", { item: nodeWidget.item, node: nodeWidget} );
			this.onClick(nodeWidget.item, nodeWidget);
			this.focusNode(nodeWidget);
		}
		dojo.stopEvent(e);
	},

	_onExpandoClick: function(/*Object*/ message){
		// summary: user clicked the +/- icon; expand or collapse my children.
		var node = message.node;
		
		// If we are collapsing, we might be hiding the currently focused node.
		// Also, clicking the expando node might have erased focus from the current node.
		// For simplicity's sake just focus on the node with the expando.
		this.focusNode(node);

		if(node.isExpanded){
			this._collapseNode(node);
		}else{
			this._expandNode(node);
		}
	},

	onClick: function(/* dojo.data */ item, /*TreeNode*/ node){
		// summary: user overridable function for executing a tree item
	},

	_getNextNode: function(node){
		// summary: get next visible node

		if(node.isExpandable && node.isExpanded && node.hasChildren()){
			// if this is an expanded node, get the first child
			return node.getChildren()[0];		// _TreeNode	
		}else{
			// find a parent node with a sibling
			while(node && node.isTreeNode){
				var returnNode = node.getNextSibling();
				if(returnNode){
					return returnNode;		// _TreeNode
				}
				node = node.getParent();
			}
			return null;
		}
	},

	_getRootOrFirstNode: function(){
		// summary: get first visible node
		return this.showRoot ? this.rootNode : this.rootNode.getChildren()[0];
	},

	_collapseNode: function(/*_TreeNode*/ node){
		// summary: called when the user has requested to collapse the node

		if(node.isExpandable){
			if(node.state == "LOADING"){
				// ignore clicks while we are in the process of loading data
				return;
			}

			node.collapse();
			if(this.persist && node.item){
				delete this._openedItemIds[this.model.getIdentity(node.item)];
				this._saveState();
			}
		}
	},

	_expandNode: function(/*_TreeNode*/ node){
		// summary: called when the user has requested to expand the node

		if(!node.isExpandable){
			return;
		}

		var model = this.model,
			item = node.item;

		switch(node.state){
			case "LOADING":
				// ignore clicks while we are in the process of loading data
				return;

			case "UNCHECKED":
				// need to load all the children, and then expand
				node.markProcessing();
				var _this = this;
				model.getChildren(item, function(items){
						node.unmarkProcessing();
						node.setChildItems(items);
						_this._expandNode(node);
					},
					function(err){
						console.error(_this, ": error loading root children: ", err);
					});
				break;

			default:
				// data is already loaded; just proceed
				node.expand();
				if(this.persist && item){
					this._openedItemIds[model.getIdentity(item)] = true;
					this._saveState();
				}
		}
	},

	////////////////// Miscellaneous functions ////////////////

	blurNode: function(){
		// summary
		//	Removes focus from the currently focused node (which must be visible).
		//	Usually not called directly (just call focusNode() on another node instead)
		var node = this.lastFocused;
		if(!node){ return; }
		var labelNode = node.labelNode;
		dojo.removeClass(labelNode, "dijitTreeLabelFocused");
		labelNode.setAttribute("tabIndex", "-1");
		dijit.setWaiState(labelNode, "selected", false);
		this.lastFocused = null;
	},

	focusNode: function(/* _tree.Node */ node){
		// summary
		//	Focus on the specified node (which must be visible)

		// set focus so that the label will be voiced using screen readers
		node.labelNode.focus();
	},

	_onBlur: function(){
		// summary:
		// 		We've moved away from the whole tree.  The currently "focused" node
		//		(see focusNode above) should remain as the lastFocused node so we can
		//		tab back into the tree.  Just change CSS to get rid of the dotted border
		//		until that time

		this.inherited(arguments);
		if(this.lastFocused){
			var labelNode = this.lastFocused.labelNode;
			dojo.removeClass(labelNode, "dijitTreeLabelFocused");	
		}
	},

	_onTreeFocus: function(/*Widget*/ node){
		// summary:
		//		called from onFocus handler of treeitem labelNode to set styles, wai state and tabindex
		//		for currently focused treeitem.
		
		if (node){
			if(node != this.lastFocused){
				this.blurNode();
			}
			var labelNode = node.labelNode;
			// set tabIndex so that the tab key can find this node
			labelNode.setAttribute("tabIndex", "0");
			dijit.setWaiState(labelNode, "selected", true);
			dojo.addClass(labelNode, "dijitTreeLabelFocused");
			this.lastFocused = node;
		}
	},

	//////////////// Events from the model //////////////////////////
	
	_onItemDelete: function(/*Object*/ item){
		//summary: delete event from the store
		// TODO: currently this isn't called, and technically doesn't need to be,
		// but it would help with garbage collection

		var identity = this.model.getIdentity(item);
		var node = this._itemNodeMap[identity];

		if(node){
			var parent = node.getParent();
			if(parent){
				// if node has not already been orphaned from a _onSetItem(parent, "children", ..) call...
				parent.removeChild(node);
			}
			delete this._itemNodeMap[identity];
			node.destroyRecursive();
		}
	},

	_onItemChange: function(/*Item*/ item){
		//summary: set data event on an item in the store
		var model = this.model,
			identity = model.getIdentity(item),
			node = this._itemNodeMap[identity];

		if(node){
			node.setLabelNode(this.getLabel(item));
			node._updateItemClasses(item);
		}
	},

	_onItemChildrenChange: function(/*dojo.data.Item*/ parent, /*dojo.data.Item[]*/ newChildrenList){
		//summary: set data event on an item in the store
		var model = this.model,
			identity = model.getIdentity(parent),
			parentNode = this._itemNodeMap[identity];

		if(parentNode){
			parentNode.setChildItems(newChildrenList);
		}
	},

	/////////////// Miscellaneous funcs
	
	_saveState: function(){
		//summary: create and save a cookie with the currently expanded nodes identifiers
		if(!this.persist){
			return;
		}
		var ary = [];
		for(var id in this._openedItemIds){
			ary.push(id);
		}
		dojo.cookie(this.cookieName, ary.join(","));
	},

	destroy: function(){
		if(this.rootNode){
			this.rootNode.destroyRecursive();
		}
		this.rootNode = null;
		this.inherited(arguments);
	},
	
	destroyRecursive: function(){
		// A tree is treated as a leaf, not as a node with children (like a grid),
		// but defining destroyRecursive for back-compat.
		this.destroy();
	}
});


dojo.declare(
	"dijit.tree.TreeStoreModel",
	null,
{
	// summary
	//		Implements dijit.Tree.model connecting to a store with a single
	//		root item.  Any methods passed into the constructor will override
	//		the ones defined here.

	// store: dojo.data.Store
	//		Underlying store
	store: null,

	// childrenAttrs: String[]
	//		one ore more attributes that holds children of a tree node
	childrenAttrs: ["children"],
	
	// root: dojo.data.Item
	//		Pointer to the root item (read only, not a parameter)
	root: null,

	// query: anything
	//		Specifies datastore query to return the root item for the tree.
	//		Must only return a single item.   Alternately can just pass in pointer
	//		to root item.
	// example:
	//		{id:'ROOT'}
	query: null,

	constructor: function(/* Object */ args){
		// summary: passed the arguments listed above (store, etc)
		dojo.mixin(this, args);

		this.connects = [];

		var store = this.store;
		if(!store.getFeatures()['dojo.data.api.Identity']){
			throw new Error("dijit.Tree: store must support dojo.data.Identity");			
		}

		// if the store supports Notification, subscribe to the notification events
		if(store.getFeatures()['dojo.data.api.Notification']){
			this.connects = this.connects.concat([
				dojo.connect(store, "onNew", this, "_onNewItem"),
				dojo.connect(store, "onDelete", this, "_onDeleteItem"),
				dojo.connect(store, "onSet", this, "_onSetItem")
			]);
		}
	},

	destroy: function(){
		dojo.forEach(this.connects, dojo.disconnect);
	},

	// =======================================================================
	// Methods for traversing hierarchy

	getRoot: function(onItem, onError){
		// summary:
		//		Calls onItem with the root item for the tree, possibly a fabricated item.
		//		Calls onError on error.
		if(this.root){
			onItem(this.root);
		}else{
			this.store.fetch({
				query: this.query,
				onComplete: dojo.hitch(this, function(items){
					if(items.length != 1){
						throw new Error(this.declaredClass + ": query " + query + " returned " + items.length +
						 	" items, but must return exactly one item");
					}
					this.root = items[0];
					onItem(this.root);
				}),
				onError: onError
			});
		}
	},

	mayHaveChildren: function(/*dojo.data.Item*/ item){
		// summary
		//		Tells if an item has or may have children.  Implementing logic here
		//		avoids showing +/- expando icon for nodes that we know don't have children.
		//		(For efficiency reasons we may not want to check if an element actually
		//		has children until user clicks the expando node)
		return dojo.some(this.childrenAttrs, function(attr){
			return this.store.hasAttribute(item, attr);
		}, this);
	},

	getChildren: function(/*dojo.data.Item*/ parentItem, /*function(items)*/ onComplete, /*function*/ onError){
		// summary
		// 		Calls onComplete() with array of child items of given parent item, all loaded.

		var store = this.store;

		// get children of specified item
		var childItems = [];
		for (var i=0; i<this.childrenAttrs.length; i++){
			var vals = store.getValues(parentItem, this.childrenAttrs[i]);
			childItems = childItems.concat(vals);
		}

		// count how many items need to be loaded
		var _waitCount = 0;
		dojo.forEach(childItems, function(item){ if(!store.isItemLoaded(item)){ _waitCount++; } });

		if(_waitCount == 0){
			// all items are already loaded.  proceed...
			onComplete(childItems);
		}else{
			// still waiting for some or all of the items to load
			var onItem = function onItem(item){
				if(--_waitCount == 0){
					// all nodes have been loaded, send them to the tree
					onComplete(childItems);
				}
			}
			dojo.forEach(childItems, function(item){
				if(!store.isItemLoaded(item)){
					store.loadItem({
						item: item,
						onItem: onItem,
						onError: onError
					});
				}
			});
		}
	},

	// =======================================================================
	// Inspecting items

	getIdentity: function(/* item */ item){
		return this.store.getIdentity(item);	// Object
	},

	getLabel: function(/*dojo.data.Item*/ item){
		// summary: get the label for an item
		return this.store.getLabel(item);	// String
	},

	// =======================================================================
	// Write interface

	newItem: function(/* Object? */ args, /*Item*/ parent){
		// summary
		//		Creates a new item.   See dojo.data.api.Write for details on args.
		//		Used in drag & drop when item from external source dropped onto tree.
		var pInfo = {parent: parent, attribute: this.childrenAttrs[0]};
		return this.store.newItem(args, pInfo);
	},

	pasteItem: function(/*Item*/ childItem, /*Item*/ oldParentItem, /*Item*/ newParentItem, /*Boolean*/ bCopy){
		// summary
		//		Move or copy an item from one parent item to another.
		//		Used in drag & drop
		var store = this.store,
			parentAttr = this.childrenAttrs[0];	// name of "children" attr in parent item

		// remove child from source item, and record the attributee that child occurred in	
		if(oldParentItem){
			dojo.forEach(this.childrenAttrs, function(attr){
				if(store.containsValue(oldParentItem, attr, childItem)){
					if(!bCopy){
						var values = dojo.filter(store.getValues(oldParentItem, attr), function(x){
							return x != childItem;
						});
						store.setValues(oldParentItem, attr, values);
					}
					parentAttr = attr;
				}
			});
		}

		// modify target item's children attribute to include this item
		if(newParentItem){
			store.setValues(newParentItem, parentAttr,
				store.getValues(newParentItem, parentAttr).concat(childItem));
		}
	},

	// =======================================================================
	// Callbacks
	
	onChange: function(/*dojo.data.Item*/ item){
		// summary
		//		Callback whenever an item has changed, so that Tree
		//		can update the label, icon, etc.   Note that changes
		//		to an item's children or parent(s) will trigger an
		//		onChildrenChange() so you can ignore those changes here.
	},

	onChildrenChange: function(/*dojo.data.Item*/ parent, /*dojo.data.Item[]*/ newChildrenList){
		// summary
		//		Callback to do notifications about new, updated, or deleted items.
	},

	// =======================================================================
	///Events from data store

	_onNewItem: function(/* dojo.data.Item */ item, /* Object */ parentInfo){
		// summary: handler for when new items appear in the store.

		//	In this case there's no correspond onSet() call on the parent of this
		//	item, so need to get the new children list of the parent manually somehow.
		if(!parentInfo){
			return;
		}
		this.getChildren(parentInfo.item, dojo.hitch(this, function(children){
			// NOTE: maybe can be optimized since parentInfo contains the new and old attribute value
			this.onChildrenChange(parentInfo.item, children);
		}));
	},
	
	_onDeleteItem: function(/*Object*/ item){
		// summary: handler for delete notifications from underlying store
	},

	_onSetItem: function(/* item */ item, 
					/* attribute-name-string */ attribute, 
					/* object | array */ oldValue,
					/* object | array */ newValue){
		//summary: set data event on an item in the store
	
		if(dojo.indexOf(this.childrenAttrs, attribute) != -1){
			// item's children list changed
			this.getChildren(item, dojo.hitch(this, function(children){
				// NOTE: maybe can be optimized since parentInfo contains the new and old attribute value
				this.onChildrenChange(item, children);
			}));
		}else{
			// item's label/icon/etc. changed.
			this.onChange(item);
		}
	}
});

dojo.declare("dijit.tree.ForestStoreModel", dijit.tree.TreeStoreModel, {
	// summary
	//		Interface between Tree and a dojo.store that doesn't have a root item, ie,
	//		has multiple "top level" items.
	//
	// description
	//		Use this class to wrap a dojo.store, making all the items matching the specified query
	//		appear as children of a fabricated "root item".  If no query is specified then all the
	//		items returned by fetch() on the underlying store become children of the root item.
	//		It allows dijit.Tree to assume a single root item, even if the store doesn't have one.

	// Parameters to constructor

	// rootId: String
	//	ID of fabricated root item
	rootId: "$root$",

	// rootLabel: String
	//	Label of fabricated root item
	rootLabel: "ROOT",

	// query: String
	//	Specifies the set of children of the root item.
	// example:
	//		{type:'continent'}
	query: null,

	// End of parameters to constructor

	constructor: function(params){
		// Make dummy root item
		this.root = {
			store: this,
			root: true,
			id: params.rootId,
			label: params.rootLabel,
			children: params.rootChildren	// optional param
		};
	},

	// =======================================================================
	// Methods for traversing hierarchy

	mayHaveChildren: function(/*dojo.data.Item*/ item){
		// summary
		//		Tells if an item has or may have children.  Implementing logic here
		//		avoids showing +/- expando icon for nodes that we know don't have children.
		//		(For efficiency reasons we may not want to check if an element actually
		//		has children until user clicks the expando node)
		return item === this.root || this.inherited(arguments);
	},

	getChildren: function(/*dojo.data.Item*/ parentItem, /*function(items)*/ callback, /*function*/ onError){
		// summary
		// 		Calls onComplete() with array of child items of given parent item, all loaded.
		if(parentItem === this.root){
			if(this.root.children){
				// already loaded, just return
				callback(this.root.children);
			}else{
				this.store.fetch({
					query: this.query,
					onComplete: dojo.hitch(this, function(items){
						this.root.children = items;
						callback(items);
					}),
					onError: onError
				});
			}
		}else{
			this.inherited(arguments);
		}
	},

	// =======================================================================
	// Inspecting items

	getIdentity: function(/* item */ item){
		return (item === this.root) ? this.root.id : this.inherited(arguments);
	},

	getLabel: function(/* item */ item){
		return	(item === this.root) ? this.root.label : this.inherited(arguments);
	},

	// =======================================================================
	// Write interface

	newItem: function(/* Object? */ args, /*Item*/ parent){
		// summary
		//		Creates a new item.   See dojo.data.api.Write for details on args.
		//		Used in drag & drop when item from external source dropped onto tree.
		if(parent===this.root){
			this.onNewRootItem(args);
			return this.store.newItem(args);
		}else{
			return this.inherited(arguments);
		}
	},
 
	onNewRootItem: function(args){
		// summary:
		//		User can override this method to modify a new element that's being
		//		added to the root of the tree, for example to add a flag like root=true
	},

	pasteItem: function(/*Item*/ childItem, /*Item*/ oldParentItem, /*Item*/ newParentItem, /*Boolean*/ bCopy){
		// summary
		//		Move or copy an item from one parent item to another.
		//		Used in drag & drop
		if(oldParentItem === this.root){
			if(!bCopy){
				// It's onLeaveRoot()'s responsibility to modify the item so it no longer matches
				// this.query... thus triggering an onChildrenChange() event to notify the Tree
				// that this element is no longer a child of the root node
				this.onLeaveRoot(childItem);
			}
		}
		dijit.tree.TreeStoreModel.prototype.pasteItem.call(this, childItem,
			oldParentItem === this.root ? null : oldParentItem,
			newParentItem === this.root ? null : newParentItem
		);
		if(newParentItem === this.root){
			// It's onAddToRoot()'s responsibility to modify the item so it matches
			// this.query... thus triggering an onChildrenChange() event to notify the Tree
			// that this element is now a child of the root node
			this.onAddToRoot(childItem);
		}
	},

	// =======================================================================
	// Callbacks
	
	onAddToRoot: function(/* item */ item){
		// summary
		//		Called when item added to root of tree; user must override
		//		to modify the item so that it matches the query for top level items
		// example
		//	|	store.setValue(item, "root", true);
		console.log(this, ": item ", item, " added to root");
	},

	onLeaveRoot: function(/* item */ item){
		// summary
		//		Called when item removed from root of tree; user must override
		//		to modify the item so it doesn't match the query for top level items
		// example
		// 	|	store.unsetAttribute(item, "root");
		console.log(this, ": item ", item, " removed from root");
	},
	
	// =======================================================================
	// Events from data store

	_requeryTop: function(){
		// reruns the query for the children of the root node,
		// sending out an onSet notification if those children have changed
		var _this = this,
			oldChildren = this.root.children;
		this.store.fetch({
			query: this.query,
			onComplete: function(newChildren){
				_this.root.children = newChildren;

				// If the list of children or the order of children has changed...	
				if(oldChildren.length != newChildren.length ||
					dojo.some(oldChildren, function(item, idx){ return newChildren[idx] != item;})){
					_this.onChildrenChange(_this.root, newChildren);
				}
			}
		});
	},

	_onNewItem: function(/* dojo.data.Item */ item, /* Object */ parentInfo){
		// summary: handler for when new items appear in the store.

		//		In theory, any new item could be a top level item.
		//		Do the safe but inefficient thing by requerying the top
		//		level items.   User can override this function to do something
		//		more efficient.
		this._requeryTop();

		this.inherited(arguments);
	},

	_onDeleteItem: function(/*Object*/ item){
		// summary: handler for delete notifications from underlying store

		// check if this was a child of root, and if so send notification that root's children
		// have changed
		if(dojo.indexOf(this.root.children, item) != -1){
			this._requeryTop();
		}

		this.inherited(arguments);
	}
});

}

if(!dojo._hasResource["dojo.data.util.filter"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.data.util.filter"] = true;
dojo.provide("dojo.data.util.filter");

dojo.data.util.filter.patternToRegExp = function(/*String*/pattern, /*boolean?*/ ignoreCase){
	//	summary:  
	//		Helper function to convert a simple pattern to a regular expression for matching.
	//	description:
	//		Returns a regular expression object that conforms to the defined conversion rules.
	//		For example:  
	//			ca*   -> /^ca.*$/
	//			*ca*  -> /^.*ca.*$/
	//			*c\*a*  -> /^.*c\*a.*$/
	//			*c\*a?*  -> /^.*c\*a..*$/
	//			and so on.
	//
	//	pattern: string
	//		A simple matching pattern to convert that follows basic rules:
	//			* Means match anything, so ca* means match anything starting with ca
	//			? Means match single character.  So, b?b will match to bob and bab, and so on.
	//      	\ is an escape character.  So for example, \* means do not treat * as a match, but literal character *.
	//				To use a \ as a character in the string, it must be escaped.  So in the pattern it should be 
	//				represented by \\ to be treated as an ordinary \ character instead of an escape.
	//
	//	ignoreCase:
	//		An optional flag to indicate if the pattern matching should be treated as case-sensitive or not when comparing
	//		By default, it is assumed case sensitive.

	var rxp = "^";
	var c = null;
	for(var i = 0; i < pattern.length; i++){
		c = pattern.charAt(i);
		switch (c) {
			case '\\':
				rxp += c;
				i++;
				rxp += pattern.charAt(i);
				break;
			case '*':
				rxp += ".*"; break;
			case '?':
				rxp += "."; break;
			case '$':
			case '^':
			case '/':
			case '+':
			case '.':
			case '|':
			case '(':
			case ')':
			case '{':
			case '}':
			case '[':
			case ']':
				rxp += "\\"; //fallthrough
			default:
				rxp += c;
		}
	}
	rxp += "$";
	if(ignoreCase){
		return new RegExp(rxp,"i"); //RegExp
	}else{
		return new RegExp(rxp); //RegExp
	}
	
};

}

if(!dojo._hasResource["dojo.data.util.sorter"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.data.util.sorter"] = true;
dojo.provide("dojo.data.util.sorter");

dojo.data.util.sorter.basicComparator = function(	/*anything*/ a, 
													/*anything*/ b){
	//	summary:  
	//		Basic comparision function that compares if an item is greater or less than another item
	//	description:  
	//		returns 1 if a > b, -1 if a < b, 0 if equal.
	//		undefined values are treated as larger values so that they're pushed to the end of the list.

	var ret = 0;
	if(a > b || typeof a === "undefined" || a === null){
		ret = 1;
	}else if(a < b || typeof b === "undefined" || b === null){
		ret = -1;
	}
	return ret; //int, {-1,0,1}
};

dojo.data.util.sorter.createSortFunction = function(	/* attributes array */sortSpec,
														/*dojo.data.core.Read*/ store){
	//	summary:  
	//		Helper function to generate the sorting function based off the list of sort attributes.
	//	description:  
	//		The sort function creation will look for a property on the store called 'comparatorMap'.  If it exists
	//		it will look in the mapping for comparisons function for the attributes.  If one is found, it will
	//		use it instead of the basic comparator, which is typically used for strings, ints, booleans, and dates.
	//		Returns the sorting function for this particular list of attributes and sorting directions.
	//
	//	sortSpec: array
	//		A JS object that array that defines out what attribute names to sort on and whether it should be descenting or asending.
	//		The objects should be formatted as follows:
	//		{
	//			attribute: "attributeName-string" || attribute,
	//			descending: true|false;   // Default is false.
	//		}
	//	store: object
	//		The datastore object to look up item values from.
	//
	var sortFunctions=[];   

	function createSortFunction(attr, dir){
		return function(itemA, itemB){
			var a = store.getValue(itemA, attr);
			var b = store.getValue(itemB, attr);
			//See if we have a override for an attribute comparison.
			var comparator = null;
			if(store.comparatorMap){
				if(typeof attr !== "string"){
					 attr = store.getIdentity(attr);
				}
				comparator = store.comparatorMap[attr]||dojo.data.util.sorter.basicComparator;
			}
			comparator = comparator||dojo.data.util.sorter.basicComparator; 
			return dir * comparator(a,b); //int
		};
	}

	for(var i = 0; i < sortSpec.length; i++){
		sortAttribute = sortSpec[i];
		if(sortAttribute.attribute){
			var direction = (sortAttribute.descending) ? -1 : 1;
			sortFunctions.push(createSortFunction(sortAttribute.attribute, direction));
		}
	}

	return function(rowA, rowB){
		var i=0;
		while(i < sortFunctions.length){
			var ret = sortFunctions[i++](rowA, rowB);
			if(ret !== 0){
				return ret;//int
			}
		}
		return 0; //int  
	};  //  Function
};

}

if(!dojo._hasResource["dojo.data.util.simpleFetch"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.data.util.simpleFetch"] = true;
dojo.provide("dojo.data.util.simpleFetch");


dojo.data.util.simpleFetch.fetch = function(/* Object? */ request){
	//	summary:
	//		The simpleFetch mixin is designed to serve as a set of function(s) that can
	//		be mixed into other datastore implementations to accelerate their development.  
	//		The simpleFetch mixin should work well for any datastore that can respond to a _fetchItems() 
	//		call by returning an array of all the found items that matched the query.  The simpleFetch mixin
	//		is not designed to work for datastores that respond to a fetch() call by incrementally
	//		loading items, or sequentially loading partial batches of the result
	//		set.  For datastores that mixin simpleFetch, simpleFetch 
	//		implements a fetch method that automatically handles eight of the fetch()
	//		arguments -- onBegin, onItem, onComplete, onError, start, count, sort and scope
	//		The class mixing in simpleFetch should not implement fetch(),
	//		but should instead implement a _fetchItems() method.  The _fetchItems() 
	//		method takes three arguments, the keywordArgs object that was passed 
	//		to fetch(), a callback function to be called when the result array is
	//		available, and an error callback to be called if something goes wrong.
	//		The _fetchItems() method should ignore any keywordArgs parameters for
	//		start, count, onBegin, onItem, onComplete, onError, sort, and scope.  
	//		The _fetchItems() method needs to correctly handle any other keywordArgs
	//		parameters, including the query parameter and any optional parameters 
	//		(such as includeChildren).  The _fetchItems() method should create an array of 
	//		result items and pass it to the fetchHandler along with the original request object 
	//		-- or, the _fetchItems() method may, if it wants to, create an new request object 
	//		with other specifics about the request that are specific to the datastore and pass 
	//		that as the request object to the handler.
	//
	//		For more information on this specific function, see dojo.data.api.Read.fetch()
	request = request || {};
	if(!request.store){
		request.store = this;
	}
	var self = this;

	var _errorHandler = function(errorData, requestObject){
		if(requestObject.onError){
			var scope = requestObject.scope || dojo.global;
			requestObject.onError.call(scope, errorData, requestObject);
		}
	};

	var _fetchHandler = function(items, requestObject){
		var oldAbortFunction = requestObject.abort || null;
		var aborted = false;

		var startIndex = requestObject.start?requestObject.start:0;
		var endIndex   = requestObject.count?(startIndex + requestObject.count):items.length;

		requestObject.abort = function(){
			aborted = true;
			if(oldAbortFunction){
				oldAbortFunction.call(requestObject);
			}
		};

		var scope = requestObject.scope || dojo.global;
		if(!requestObject.store){
			requestObject.store = self;
		}
		if(requestObject.onBegin){
			requestObject.onBegin.call(scope, items.length, requestObject);
		}
		if(requestObject.sort){
			items.sort(dojo.data.util.sorter.createSortFunction(requestObject.sort, self));
		}
		if(requestObject.onItem){
			for(var i = startIndex; (i < items.length) && (i < endIndex); ++i){
				var item = items[i];
				if(!aborted){
					requestObject.onItem.call(scope, item, requestObject);
				}
			}
		}
		if(requestObject.onComplete && !aborted){
			var subset = null;
			if (!requestObject.onItem) {
				subset = items.slice(startIndex, endIndex);
			}
			requestObject.onComplete.call(scope, subset, requestObject);   
		}
	};
	this._fetchItems(request, _fetchHandler, _errorHandler);
	return request;	// Object
};

}

if(!dojo._hasResource["dojo.data.ItemFileReadStore"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.data.ItemFileReadStore"] = true;
dojo.provide("dojo.data.ItemFileReadStore");





dojo.declare("dojo.data.ItemFileReadStore", null,{
	//	summary:
	//		The ItemFileReadStore implements the dojo.data.api.Read API and reads
	//		data from JSON files that have contents in this format --
	//		{ items: [
	//			{ name:'Kermit', color:'green', age:12, friends:['Gonzo', {_reference:{name:'Fozzie Bear'}}]},
	//			{ name:'Fozzie Bear', wears:['hat', 'tie']},
	//			{ name:'Miss Piggy', pets:'Foo-Foo'}
	//		]}
	//		Note that it can also contain an 'identifer' property that specified which attribute on the items 
	//		in the array of items that acts as the unique identifier for that item.
	//
	constructor: function(/* Object */ keywordParameters){
		//	summary: constructor
		//	keywordParameters: {url: String}
		//	keywordParameters: {data: jsonObject}
		//	keywordParameters: {typeMap: object)
		//		The structure of the typeMap object is as follows:
		//		{
		//			type0: function || object,
		//			type1: function || object,
		//			...
		//			typeN: function || object
		//		}
		//		Where if it is a function, it is assumed to be an object constructor that takes the 
		//		value of _value as the initialization parameters.  If it is an object, then it is assumed
		//		to be an object of general form:
		//		{
		//			type: function, //constructor.
		//			deserialize:	function(value) //The function that parses the value and constructs the object defined by type appropriately.
		//		}
	
		this._arrayOfAllItems = [];
		this._arrayOfTopLevelItems = [];
		this._loadFinished = false;
		this._jsonFileUrl = keywordParameters.url;
		this._jsonData = keywordParameters.data;
		this._datatypeMap = keywordParameters.typeMap || {};
		if(!this._datatypeMap['Date']){
			//If no default mapping for dates, then set this as default.
			//We use the dojo.date.stamp here because the ISO format is the 'dojo way'
			//of generically representing dates.
			this._datatypeMap['Date'] = {
											type: Date,
											deserialize: function(value){
												return dojo.date.stamp.fromISOString(value);
											}
										};
		}
		this._features = {'dojo.data.api.Read':true, 'dojo.data.api.Identity':true};
		this._itemsByIdentity = null;
		this._storeRefPropName = "_S";  // Default name for the store reference to attach to every item.
		this._itemNumPropName = "_0"; // Default Item Id for isItem to attach to every item.
		this._rootItemPropName = "_RI"; // Default Item Id for isItem to attach to every item.
		this._reverseRefMap = "_RRM"; // Default attribute for constructing a reverse reference map for use with reference integrity
		this._loadInProgress = false;	//Got to track the initial load to prevent duelling loads of the dataset.
		this._queuedFetches = [];
	},
	
	url: "",	// use "" rather than undefined for the benefit of the parser (#3539)

	_assertIsItem: function(/* item */ item){
		//	summary:
		//		This function tests whether the item passed in is indeed an item in the store.
		//	item: 
		//		The item to test for being contained by the store.
		if(!this.isItem(item)){ 
			throw new Error("dojo.data.ItemFileReadStore: Invalid item argument.");
		}
	},

	_assertIsAttribute: function(/* attribute-name-string */ attribute){
		//	summary:
		//		This function tests whether the item passed in is indeed a valid 'attribute' like type for the store.
		//	attribute: 
		//		The attribute to test for being contained by the store.
		if(typeof attribute !== "string"){ 
			throw new Error("dojo.data.ItemFileReadStore: Invalid attribute argument.");
		}
	},

	getValue: function(	/* item */ item, 
						/* attribute-name-string */ attribute, 
						/* value? */ defaultValue){
		//	summary: 
		//		See dojo.data.api.Read.getValue()
		var values = this.getValues(item, attribute);
		return (values.length > 0)?values[0]:defaultValue; // mixed
	},

	getValues: function(/* item */ item, 
						/* attribute-name-string */ attribute){
		//	summary: 
		//		See dojo.data.api.Read.getValues()

		this._assertIsItem(item);
		this._assertIsAttribute(attribute);
		return item[attribute] || []; // Array
	},

	getAttributes: function(/* item */ item){
		//	summary: 
		//		See dojo.data.api.Read.getAttributes()
		this._assertIsItem(item);
		var attributes = [];
		for(var key in item){
			// Save off only the real item attributes, not the special id marks for O(1) isItem.
			if((key !== this._storeRefPropName) && (key !== this._itemNumPropName) && (key !== this._rootItemPropName) && (key !== this._reverseRefMap)){
				attributes.push(key);
			}
		}
		return attributes; // Array
	},

	hasAttribute: function(	/* item */ item,
							/* attribute-name-string */ attribute) {
		//	summary: 
		//		See dojo.data.api.Read.hasAttribute()
		return this.getValues(item, attribute).length > 0;
	},

	containsValue: function(/* item */ item, 
							/* attribute-name-string */ attribute, 
							/* anything */ value){
		//	summary: 
		//		See dojo.data.api.Read.containsValue()
		var regexp = undefined;
		if(typeof value === "string"){
			regexp = dojo.data.util.filter.patternToRegExp(value, false);
		}
		return this._containsValue(item, attribute, value, regexp); //boolean.
	},

	_containsValue: function(	/* item */ item, 
								/* attribute-name-string */ attribute, 
								/* anything */ value,
								/* RegExp?*/ regexp){
		//	summary: 
		//		Internal function for looking at the values contained by the item.
		//	description: 
		//		Internal function for looking at the values contained by the item.  This 
		//		function allows for denoting if the comparison should be case sensitive for
		//		strings or not (for handling filtering cases where string case should not matter)
		//	
		//	item:
		//		The data item to examine for attribute values.
		//	attribute:
		//		The attribute to inspect.
		//	value:	
		//		The value to match.
		//	regexp:
		//		Optional regular expression generated off value if value was of string type to handle wildcarding.
		//		If present and attribute values are string, then it can be used for comparison instead of 'value'
		return dojo.some(this.getValues(item, attribute), function(possibleValue){
			if(possibleValue !== null && !dojo.isObject(possibleValue) && regexp){
				if(possibleValue.toString().match(regexp)){
					return true; // Boolean
				}
			}else if(value === possibleValue){
				return true; // Boolean
			}
		});
	},

	isItem: function(/* anything */ something){
		//	summary: 
		//		See dojo.data.api.Read.isItem()
		if(something && something[this._storeRefPropName] === this){
			if(this._arrayOfAllItems[something[this._itemNumPropName]] === something){
				return true;
			}
		}
		return false; // Boolean
	},

	isItemLoaded: function(/* anything */ something){
		//	summary: 
		//		See dojo.data.api.Read.isItemLoaded()
		return this.isItem(something); //boolean
	},

	loadItem: function(/* object */ keywordArgs){
		//	summary: 
		//		See dojo.data.api.Read.loadItem()
		this._assertIsItem(keywordArgs.item);
	},

	getFeatures: function(){
		//	summary: 
		//		See dojo.data.api.Read.getFeatures()
		return this._features; //Object
	},

	getLabel: function(/* item */ item){
		//	summary: 
		//		See dojo.data.api.Read.getLabel()
		if(this._labelAttr && this.isItem(item)){
			return this.getValue(item,this._labelAttr); //String
		}
		return undefined; //undefined
	},

	getLabelAttributes: function(/* item */ item){
		//	summary: 
		//		See dojo.data.api.Read.getLabelAttributes()
		if(this._labelAttr){
			return [this._labelAttr]; //array
		}
		return null; //null
	},

	_fetchItems: function(	/* Object */ keywordArgs, 
							/* Function */ findCallback, 
							/* Function */ errorCallback){
		//	summary: 
		//		See dojo.data.util.simpleFetch.fetch()
		var self = this;
		var filter = function(requestArgs, arrayOfItems){
			var items = [];
			if(requestArgs.query){
				var ignoreCase = requestArgs.queryOptions ? requestArgs.queryOptions.ignoreCase : false; 

				//See if there are any string values that can be regexp parsed first to avoid multiple regexp gens on the
				//same value for each item examined.  Much more efficient.
				var regexpList = {};
				for(var key in requestArgs.query){
					var value = requestArgs.query[key];
					if(typeof value === "string"){
						regexpList[key] = dojo.data.util.filter.patternToRegExp(value, ignoreCase);
					}
				}

				for(var i = 0; i < arrayOfItems.length; ++i){
					var match = true;
					var candidateItem = arrayOfItems[i];
					if(candidateItem === null){
						match = false;
					}else{
						for(var key in requestArgs.query) {
							var value = requestArgs.query[key];
							if (!self._containsValue(candidateItem, key, value, regexpList[key])){
								match = false;
							}
						}
					}
					if(match){
						items.push(candidateItem);
					}
				}
				findCallback(items, requestArgs);
			}else{
				// We want a copy to pass back in case the parent wishes to sort the array. 
				// We shouldn't allow resort of the internal list, so that multiple callers 
				// can get lists and sort without affecting each other.  We also need to
				// filter out any null values that have been left as a result of deleteItem()
				// calls in ItemFileWriteStore.
				for(var i = 0; i < arrayOfItems.length; ++i){
					var item = arrayOfItems[i];
					if(item !== null){
						items.push(item);
					}
				}
				findCallback(items, requestArgs);
			}
		};

		if(this._loadFinished){
			filter(keywordArgs, this._getItemsArray(keywordArgs.queryOptions));
		}else{

			if(this._jsonFileUrl){
				//If fetches come in before the loading has finished, but while
				//a load is in progress, we have to defer the fetching to be 
				//invoked in the callback.
				if(this._loadInProgress){
					this._queuedFetches.push({args: keywordArgs, filter: filter});
				}else{
					this._loadInProgress = true;
					var getArgs = {
							url: self._jsonFileUrl, 
							handleAs: "json-comment-optional"
						};
					var getHandler = dojo.xhrGet(getArgs);
					getHandler.addCallback(function(data){
						try{
							self._getItemsFromLoadedData(data);
							self._loadFinished = true;
							self._loadInProgress = false;
							
							filter(keywordArgs, self._getItemsArray(keywordArgs.queryOptions));
							self._handleQueuedFetches();
						}catch(e){
							self._loadFinished = true;
							self._loadInProgress = false;
							errorCallback(e, keywordArgs);
						}
					});
					getHandler.addErrback(function(error){
						self._loadInProgress = false;
						errorCallback(error, keywordArgs);
					});
				}
			}else if(this._jsonData){
				try{
					this._loadFinished = true;
					this._getItemsFromLoadedData(this._jsonData);
					this._jsonData = null;
					filter(keywordArgs, this._getItemsArray(keywordArgs.queryOptions));
				}catch(e){
					errorCallback(e, keywordArgs);
				}
			}else{
				errorCallback(new Error("dojo.data.ItemFileReadStore: No JSON source data was provided as either URL or a nested Javascript object."), keywordArgs);
			}
		}
	},

	_handleQueuedFetches: function(){
		//	summary: 
		//		Internal function to execute delayed request in the store.
		//Execute any deferred fetches now.
		if (this._queuedFetches.length > 0) {
			for(var i = 0; i < this._queuedFetches.length; i++){
				var fData = this._queuedFetches[i];
				var delayedQuery = fData.args;
				var delayedFilter = fData.filter;
				if(delayedFilter){
					delayedFilter(delayedQuery, this._getItemsArray(delayedQuery.queryOptions)); 
				}else{
					this.fetchItemByIdentity(delayedQuery);
				}
			}
			this._queuedFetches = [];
		}
	},

	_getItemsArray: function(/*object?*/queryOptions){
		//	summary: 
		//		Internal function to determine which list of items to search over.
		//	queryOptions: The query options parameter, if any.
		if(queryOptions && queryOptions.deep) {
			return this._arrayOfAllItems; 
		}
		return this._arrayOfTopLevelItems;
	},

	close: function(/*dojo.data.api.Request || keywordArgs || null */ request){
		 //	summary: 
		 //		See dojo.data.api.Read.close()
	},

	_getItemsFromLoadedData: function(/* Object */ dataObject){
		//	summary:
		//		Function to parse the loaded data into item format and build the internal items array.
		//	description:
		//		Function to parse the loaded data into item format and build the internal items array.
		//
		//	dataObject:
		//		The JS data object containing the raw data to convery into item format.
		//
		// 	returns: array
		//		Array of items in store item format.
		
		// First, we define a couple little utility functions...
		
		function valueIsAnItem(/* anything */ aValue){
			// summary:
			//		Given any sort of value that could be in the raw json data,
			//		return true if we should interpret the value as being an
			//		item itself, rather than a literal value or a reference.
			// example:
			// 	|	false == valueIsAnItem("Kermit");
			// 	|	false == valueIsAnItem(42);
			// 	|	false == valueIsAnItem(new Date());
			// 	|	false == valueIsAnItem({_type:'Date', _value:'May 14, 1802'});
			// 	|	false == valueIsAnItem({_reference:'Kermit'});
			// 	|	true == valueIsAnItem({name:'Kermit', color:'green'});
			// 	|	true == valueIsAnItem({iggy:'pop'});
			// 	|	true == valueIsAnItem({foo:42});
			var isItem = (
				(aValue != null) &&
				(typeof aValue == "object") &&
				(!dojo.isArray(aValue)) &&
				(!dojo.isFunction(aValue)) &&
				(aValue.constructor == Object) &&
				(typeof aValue._reference == "undefined") && 
				(typeof aValue._type == "undefined") && 
				(typeof aValue._value == "undefined")
			);
			return isItem;
		}
		
		var self = this;
		function addItemAndSubItemsToArrayOfAllItems(/* Item */ anItem){
			self._arrayOfAllItems.push(anItem);
			for(var attribute in anItem){
				var valueForAttribute = anItem[attribute];
				if(valueForAttribute){
					if(dojo.isArray(valueForAttribute)){
						var valueArray = valueForAttribute;
						for(var k = 0; k < valueArray.length; ++k){
							var singleValue = valueArray[k];
							if(valueIsAnItem(singleValue)){
								addItemAndSubItemsToArrayOfAllItems(singleValue);
							}
						}
					}else{
						if(valueIsAnItem(valueForAttribute)){
							addItemAndSubItemsToArrayOfAllItems(valueForAttribute);
						}
					}
				}
			}
		}

		this._labelAttr = dataObject.label;

		// We need to do some transformations to convert the data structure
		// that we read from the file into a format that will be convenient
		// to work with in memory.

		// Step 1: Walk through the object hierarchy and build a list of all items
		var i;
		var item;
		this._arrayOfAllItems = [];
		this._arrayOfTopLevelItems = dataObject.items;

		for(i = 0; i < this._arrayOfTopLevelItems.length; ++i){
			item = this._arrayOfTopLevelItems[i];
			addItemAndSubItemsToArrayOfAllItems(item);
			item[this._rootItemPropName]=true;
		}

		// Step 2: Walk through all the attribute values of all the items, 
		// and replace single values with arrays.  For example, we change this:
		//		{ name:'Miss Piggy', pets:'Foo-Foo'}
		// into this:
		//		{ name:['Miss Piggy'], pets:['Foo-Foo']}
		// 
		// We also store the attribute names so we can validate our store  
		// reference and item id special properties for the O(1) isItem
		var allAttributeNames = {};
		var key;

		for(i = 0; i < this._arrayOfAllItems.length; ++i){
			item = this._arrayOfAllItems[i];
			for(key in item){
				if (key !== this._rootItemPropName)
				{
					var value = item[key];
					if(value !== null){
						if(!dojo.isArray(value)){
							item[key] = [value];
						}
					}else{
						item[key] = [null];
					}
				}
				allAttributeNames[key]=key;
			}
		}

		// Step 3: Build unique property names to use for the _storeRefPropName and _itemNumPropName
		// This should go really fast, it will generally never even run the loop.
		while(allAttributeNames[this._storeRefPropName]){
			this._storeRefPropName += "_";
		}
		while(allAttributeNames[this._itemNumPropName]){
			this._itemNumPropName += "_";
		}
		while(allAttributeNames[this._reverseRefMap]){
			this._reverseRefMap += "_";
		}

		// Step 4: Some data files specify an optional 'identifier', which is 
		// the name of an attribute that holds the identity of each item. 
		// If this data file specified an identifier attribute, then build a 
		// hash table of items keyed by the identity of the items.
		var arrayOfValues;

		var identifier = dataObject.identifier;
		if(identifier){
			this._itemsByIdentity = {};
			this._features['dojo.data.api.Identity'] = identifier;
			for(i = 0; i < this._arrayOfAllItems.length; ++i){
				item = this._arrayOfAllItems[i];
				arrayOfValues = item[identifier];
				var identity = arrayOfValues[0];
				if(!this._itemsByIdentity[identity]){
					this._itemsByIdentity[identity] = item;
				}else{
					if(this._jsonFileUrl){
						throw new Error("dojo.data.ItemFileReadStore:  The json data as specified by: [" + this._jsonFileUrl + "] is malformed.  Items within the list have identifier: [" + identifier + "].  Value collided: [" + identity + "]");
					}else if(this._jsonData){
						throw new Error("dojo.data.ItemFileReadStore:  The json data provided by the creation arguments is malformed.  Items within the list have identifier: [" + identifier + "].  Value collided: [" + identity + "]");
					}
				}
			}
		}else{
			this._features['dojo.data.api.Identity'] = Number;
		}

		// Step 5: Walk through all the items, and set each item's properties 
		// for _storeRefPropName and _itemNumPropName, so that store.isItem() will return true.
		for(i = 0; i < this._arrayOfAllItems.length; ++i){
			item = this._arrayOfAllItems[i];
			item[this._storeRefPropName] = this;
			item[this._itemNumPropName] = i;
		}

		// Step 6: We walk through all the attribute values of all the items,
		// looking for type/value literals and item-references.
		//
		// We replace item-references with pointers to items.  For example, we change:
		//		{ name:['Kermit'], friends:[{_reference:{name:'Miss Piggy'}}] }
		// into this:
		//		{ name:['Kermit'], friends:[miss_piggy] } 
		// (where miss_piggy is the object representing the 'Miss Piggy' item).
		//
		// We replace type/value pairs with typed-literals.  For example, we change:
		//		{ name:['Nelson Mandela'], born:[{_type:'Date', _value:'July 18, 1918'}] }
		// into this:
		//		{ name:['Kermit'], born:(new Date('July 18, 1918')) } 
		//
		// We also generate the associate map for all items for the O(1) isItem function.
		for(i = 0; i < this._arrayOfAllItems.length; ++i){
			item = this._arrayOfAllItems[i]; // example: { name:['Kermit'], friends:[{_reference:{name:'Miss Piggy'}}] }
			for(key in item){
				arrayOfValues = item[key]; // example: [{_reference:{name:'Miss Piggy'}}]
				for(var j = 0; j < arrayOfValues.length; ++j) {
					value = arrayOfValues[j]; // example: {_reference:{name:'Miss Piggy'}}
					if(value !== null && typeof value == "object"){
						if(value._type && value._value){
							var type = value._type; // examples: 'Date', 'Color', or 'ComplexNumber'
							var mappingObj = this._datatypeMap[type]; // examples: Date, dojo.Color, foo.math.ComplexNumber, {type: dojo.Color, deserialize(value){ return new dojo.Color(value)}}
							if(!mappingObj){ 
								throw new Error("dojo.data.ItemFileReadStore: in the typeMap constructor arg, no object class was specified for the datatype '" + type + "'");
							}else if(dojo.isFunction(mappingObj)){
								arrayOfValues[j] = new mappingObj(value._value);
							}else if(dojo.isFunction(mappingObj.deserialize)){
								arrayOfValues[j] = mappingObj.deserialize(value._value);
							}else{
								throw new Error("dojo.data.ItemFileReadStore: Value provided in typeMap was neither a constructor, nor a an object with a deserialize function");
							}
						}
						if(value._reference){
							var referenceDescription = value._reference; // example: {name:'Miss Piggy'}
							if(!dojo.isObject(referenceDescription)){
								// example: 'Miss Piggy'
								// from an item like: { name:['Kermit'], friends:[{_reference:'Miss Piggy'}]}
								arrayOfValues[j] = this._itemsByIdentity[referenceDescription];
							}else{
								// example: {name:'Miss Piggy'}
								// from an item like: { name:['Kermit'], friends:[{_reference:{name:'Miss Piggy'}}] }
								for(var k = 0; k < this._arrayOfAllItems.length; ++k){
									var candidateItem = this._arrayOfAllItems[k];
									var found = true;
									for(var refKey in referenceDescription){
										if(candidateItem[refKey] != referenceDescription[refKey]){ 
											found = false; 
										}
									}
									if(found){ 
										arrayOfValues[j] = candidateItem; 
									}
								}
							}
							if(this.referenceIntegrity){
								var refItem = arrayOfValues[j];
								if(this.isItem(refItem)){
									this._addReferenceToMap(refItem, item, key);
								}
							}
						}else if(this.isItem(value)){
							//It's a child item (not one referenced through _reference).  
							//We need to treat this as a referenced item, so it can be cleaned up
							//in a write store easily.
							if(this.referenceIntegrity){
								this._addReferenceToMap(value, item, key);
							}
						}
					}
				}
			}
		}
	},

	_addReferenceToMap: function(/*item*/ refItem, /*item*/ parentItem, /*string*/ attribute){
		 //	summary:
		 //		Method to add an reference map entry for an item and attribute.
		 //	description:
		 //		Method to add an reference map entry for an item and attribute. 		 //
		 //	refItem:
		 //		The item that is referenced.
		 //	parentItem:
		 //		The item that holds the new reference to refItem.
		 //	attribute:
		 //		The attribute on parentItem that contains the new reference.
		 
		 //Stub function, does nothing.  Real processing is in ItemFileWriteStore.
	},

	getIdentity: function(/* item */ item){
		//	summary: 
		//		See dojo.data.api.Identity.getIdentity()
		var identifier = this._features['dojo.data.api.Identity'];
		if(identifier === Number){
			return item[this._itemNumPropName]; // Number
		}else{
			var arrayOfValues = item[identifier];
			if(arrayOfValues){
				return arrayOfValues[0]; // Object || String
			}
		}
		return null; // null
	},

	fetchItemByIdentity: function(/* Object */ keywordArgs){
		//	summary: 
		//		See dojo.data.api.Identity.fetchItemByIdentity()

		// Hasn't loaded yet, we have to trigger the load.
		if(!this._loadFinished){
			var self = this;
			if(this._jsonFileUrl){

				if(this._loadInProgress){
					this._queuedFetches.push({args: keywordArgs});
				}else{
					this._loadInProgress = true;
					var getArgs = {
							url: self._jsonFileUrl, 
							handleAs: "json-comment-optional"
					};
					var getHandler = dojo.xhrGet(getArgs);
					getHandler.addCallback(function(data){
						var scope =  keywordArgs.scope?keywordArgs.scope:dojo.global;
						try{
							self._getItemsFromLoadedData(data);
							self._loadFinished = true;
							self._loadInProgress = false;
							var item = self._getItemByIdentity(keywordArgs.identity);
							if(keywordArgs.onItem){
								keywordArgs.onItem.call(scope, item);
							}
							self._handleQueuedFetches();
						}catch(error){
							self._loadInProgress = false;
							if(keywordArgs.onError){
								keywordArgs.onError.call(scope, error);
							}
						}
					});
					getHandler.addErrback(function(error){
						self._loadInProgress = false;
						if(keywordArgs.onError){
							var scope =  keywordArgs.scope?keywordArgs.scope:dojo.global;
							keywordArgs.onError.call(scope, error);
						}
					});
				}

			}else if(this._jsonData){
				// Passed in data, no need to xhr.
				self._getItemsFromLoadedData(self._jsonData);
				self._jsonData = null;
				self._loadFinished = true;
				var item = self._getItemByIdentity(keywordArgs.identity);
				if(keywordArgs.onItem){
					var scope =  keywordArgs.scope?keywordArgs.scope:dojo.global;
					keywordArgs.onItem.call(scope, item);
				}
			} 
		}else{
			// Already loaded.  We can just look it up and call back.
			var item = this._getItemByIdentity(keywordArgs.identity);
			if(keywordArgs.onItem){
				var scope =  keywordArgs.scope?keywordArgs.scope:dojo.global;
				keywordArgs.onItem.call(scope, item);
			}
		}
	},

	_getItemByIdentity: function(/* Object */ identity){
		//	summary:
		//		Internal function to look an item up by its identity map.
		var item = null;
		if(this._itemsByIdentity){
			item = this._itemsByIdentity[identity];
		}else{
			item = this._arrayOfAllItems[identity];
		}
		if(item === undefined){
			item = null;
		}
		return item; // Object
	},

	getIdentityAttributes: function(/* item */ item){
		//	summary: 
		//		See dojo.data.api.Identity.getIdentifierAttributes()
		 
		var identifier = this._features['dojo.data.api.Identity'];
		if(identifier === Number){
			// If (identifier === Number) it means getIdentity() just returns
			// an integer item-number for each item.  The dojo.data.api.Identity
			// spec says we need to return null if the identity is not composed 
			// of attributes 
			return null; // null
		}else{
			return [identifier]; // Array
		}
	},
	
	_forceLoad: function(){
		//	summary: 
		//		Internal function to force a load of the store if it hasn't occurred yet.  This is required
		//		for specific functions to work properly.  
		var self = this;
		if(this._jsonFileUrl){
				var getArgs = {
					url: self._jsonFileUrl, 
					handleAs: "json-comment-optional",
					sync: true
				};
			var getHandler = dojo.xhrGet(getArgs);
			getHandler.addCallback(function(data){
				try{
					//Check to be sure there wasn't another load going on concurrently 
					//So we don't clobber data that comes in on it.  If there is a load going on
					//then do not save this data.  It will potentially clobber current data.
					//We mainly wanted to sync/wait here.
					//TODO:  Revisit the loading scheme of this store to improve multi-initial
					//request handling.
					if (self._loadInProgress !== true && !self._loadFinished) {
						self._getItemsFromLoadedData(data);
						self._loadFinished = true;
					}
				}catch(e){
					console.log(e);
					throw e;
				}
			});
			getHandler.addErrback(function(error){
				throw error;
			});
		}else if(this._jsonData){
			self._getItemsFromLoadedData(self._jsonData);
			self._jsonData = null;
			self._loadFinished = true;
		} 
	}
});
//Mix in the simple fetch implementation to this class.
dojo.extend(dojo.data.ItemFileReadStore,dojo.data.util.simpleFetch);

}

