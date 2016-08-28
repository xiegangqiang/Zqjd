var userInfo = null;
var webScoket = null;
Ext.define('SystemApp.View.Layout.TopPanel', {
    extend: 'Ext.Container',
    alias: 'widget.layout.toppanel',
    id: 'app-header',
    height: 60,
    region: 'north',
    layout: {
        type: 'hbox',
        align: 'middle'
    },
    initComponent: function() {
    	Ext.Ajax.request({
    	    url: '/admin/admin.do?info',
    	    async :  false,
    	    success: function(response){
    	    	var res = Ext.decode(response.responseText);
    	    	userInfo = res;
    	    }
    	});
    	
        this.items = [{
            xtype: 'component',
            id: 'app-header-title',
            html: '微信后台管理系统',
            height: "48",
            flex: 0.55
        }, {
        	xtype:"container",
        	flex: 0.45,
		    layout: 'column',
        	items: [{
        		//labelStyle: 'color: red;font-weight: bold;',
	    		labelWidth: 110,
            	xtype: 'displayfield',
            	labelAlign: 'right',
            	cls: 'field-display',
            	fieldLabel: '离系统退出时间',
                value: '00:59:59',
                listeners: {
			      render: function(me, eOpts) {
			       		Ext.TaskManager.start({
					    run: function() {
					    	var time = Ext.Date.parse(me.getValue(), 'H:i:s').getTime();
					    	me.setValue(Ext.Date.format(new Date(time-100), 'H:i:s'));
					    	if(me.getValue()==='00:00:00') location.href = "/admin/logout";
					    },
					    interval: 1000
					  });
			      }
			    }
        	}, {xtype: 'component', html: '　'}, {
        		xtype: 'button',
	        	iconCls: 'user',
	        	text: "欢迎您: " + userInfo.name,
	        	menu: [{
	        		text: '修改账号信息',
	        		scope: this,
	        		handler: function(me) {
	        			this.editWin.show();
	        			this.editWin.down('form').getForm().reset();
	        		}
	        	}, {
					text: '注销用户',
					handler: function() {
						location.href = "/admin/logout";
					}
	        	}]
        	}, {xtype: 'component', html: '　'}, {
        		xtype: 'button',
	        	iconCls: 'logout',
	        	text: '退出系统',
	        	handler: function() {
	        		Ext.MessageBox.confirm('确认', '是否确认退出本系统?', function(btn) {
			    		if (btn != 'yes') return;
						location.href = "/admin/logout";
					});
	        	}
        	}, {xtype: 'component', html: '　'}, {
        		xtype: 'displayfield',
	    		labelWidth: 60,
	    		labelAlign: 'right',
        		fieldLabel: '在线人数',
	        	value: "数据加载中..."
	        }]
        }];
        this.createEditWindows();
        this.createWebScoket();
        this.callParent();
    },
    
    createEditWindows: function() {
    	 Ext.apply(Ext.form.field.VTypes, {
            password: function(val, field) {
                if (field.initialPassField) {
                    var pwd = field.up('form').down('#' + field.initialPassField);
                    return (val == pwd.getValue());
                }
                return true;
            },
            passwordText: '登录密码与确认密码不一致'
        });
        
		this.editWin = Ext.create('Ext.window.Window', {
    	    title: '修改个人账号信息',
    	    height: 210,
    	    width: 350,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	fieldDefaults: {
    	           labelWidth: 60,
    	           labelAlign: 'left',
    	           anchor: '100%',
    	           labelStyle: 'color:green;padding-left:4px'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id',
    	        	value: userInfo.id
    	        }, {
    	        	xtype: 'hiddenfield',
    	        	name: 'roles',
    	        	value: userInfo.roles
    	        }, {
    	        	xtype: 'textfield',
    	        	name: 'name',
    	        	fieldLabel: '姓名',
    	        	msgTarget: 'side',
    	            allowBlank: false,
    	            emptyText: '输入您的姓名，支持中、英文',
    	        	value: userInfo.name
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'username',
    	            fieldLabel: '用户名',
    	            msgTarget: 'side',
    	            allowBlank: false,
    	            emptyText: '用于本系统登陆和找回密码，6位以上英文、数字',
    	            value: userInfo.username
    	        }, {
	        		xtype: 'textfield',
    	        	inputType: 'password',
    	            fieldLabel: '登录密码',
    	            name: 'password',
    	            itemId: 'pass',
    	            msgTarget: 'side',
    	            allowBlank: false,
    	            emptyText: '输入您的密码',
    	            listeners: {
    	                validitychange: function(field){
    	                    field.next().validate();
    	                },
    	                blur: function(field){
    	                    field.next().validate();
    	                }
    	            }
    	        }, {
    	        	xtype: 'textfield',
    	        	inputType: 'password',
    	            fieldLabel: '确认密码',
    	            name: 'pass-cfrm',
    	            vtype: 'password',
    	            initialPassField: 'pass',
    	            msgTarget: 'side',
    	            allowBlank: false,
    	            emptyText: '确认密码'
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitFormDate
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.editWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    },
    
    submitFormDate: function() {
    	var win = this.editWin;
		var form = win.down('form');
		var name = form.getForm().findField('name').getValue();
		var namebutton = this.items.get(1).items.get(2);
    	form.submit({
			url: '/admin/admin.do?update',
			params: {
				roles: form.getForm().findField('roles')
			},
		    success: function(form, action) {
		       win.hide();
		       namebutton.setText("欢迎您: "+name);
		       Ext.Msg.alert('提示', action.result.title);
		    },
		    failure: function(form, action) {
		    	Ext.Msg.alert('提示', action.result.title);
		    }
		});
	},
	
	createWebScoket: function() {
		webScoket = new WebSocket('ws://'+window.location.host+'/websocket.do');
		var date = Ext.Date.format(new Date(), 'Y-m-d H:i:s');
		webScoket.onopen = function () {
           webScoket.send('{"type": 0, "fromUser": "' + userInfo.id + '", "date": "' + date + '", "content": ""}');
        };
        webScoket.onmessage = function (event) {
            var msgJson = eval("("+event.data+")");
            if(msgJson.toUser === "ALL" && msgJson.type < 2) Ext.getCmp('app-header').items.get(1).items.get(6).setValue(msgJson.content);
            if(msgJson.toUser === userInfo.id && msgJson.type === 2) {alert('提示，你已被管理员强制下线');location.href = "/admin/logout";}
        };
        webScoket.onclose = function (event) {
            //var msgJson=eval("("+event.data+")");
            console.log(event.data);
        };
        
        window.onbeforeunload = this.onbeforeunload_handler;
	},
	
	onbeforeunload_handler:  function() {   
	    webScoket.send('{"type": 1, "fromUser": "' + userInfo.id + '", "date": "' + Ext.Date.format(new Date(), 'Y-m-d H:i:s') + '", "content": ""}');
		webScoket.close();
        //return "确认退出?";   
    }
   
});
