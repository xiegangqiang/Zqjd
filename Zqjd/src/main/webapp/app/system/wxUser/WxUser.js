Ext.define('SystemApp.View.WxUser', {
    extend: 'Ext.panel.Panel',
    xtype: 'wxUser',
    
    initComponent: function() {
    	Ext.apply(this, {
            layout: 'border',
            items: [this.createLeftPanel(), this.createRightPanel()]
        });
        this.createWindows();
        this.callParent(arguments);
    },
    
    createLeftPanel: function() {
    	 this.groupstore = Ext.create('Ext.data.Store', {
    	    fields:['id', 'groupId', 'name', 'count', 'wx'],
    	    proxy: {
    	  	    type: 'ajax',
    	        url: '/admin/wxUser.do?group'
    	    },
            autoLoad: true
    	});
    	
    	this.groupgrid = Ext.create('Ext.panel.Panel', {
    		region: 'west',
            width: 225,
            //split: true,
            border: 0,
            collapsible: true,
            minWidth: 250,
            title: '分组信息',
            items: [{
        	    xtype: 'dataview',
				autoScroll: true,
				store: this.groupstore,
	            cls: 'wxgroup-list',
	            itemSelector: '.wxgroup-list-item',
	            overItemCls: 'wxgroup-list-item-hover',
	            tpl: '<tpl for="."><div class="wxgroup-list-item"><img src="{img}"/><tpl if="count < 3">　</tpl><span>{name} ({count})</span></div></tpl>',
                listeners: {
                    scope: this,
                    itemclick: this.switchGroup,
                    itemcontextmenu: function(fn, record, item, index, e, eOpts) {
                    	if(record.get('groupId') < 3) return;  
	    	    		this.selectrecord = record;
	    	    		this.contextMenu.showAt(e.getX(), e.getY());
	    	            e.preventDefault();
	    	    	}
                }
            }],
            tbar: ['->', {
            	xtype: 'button',
            	text: '添加分组',
            	iconCls: 'icon-add',
            	scope: this,
                handler: this.addGroup
            }]
    	});
   		return this.groupgrid;
    },
    
    switchGroup: function(me, record, item, index, e, eOpts) {
		var store = this.usergrid.getStore();
		store.currentPage = 1;
    	store.proxy.extraParams = {
			groupid: record.get('groupId')
		};
		store.reload();
    },
    
    addGroup: function(me) {
    	var form = this.groupWin.down('form');
    	form.getForm().reset();
    	var position = me.getPosition();
    	this.groupWin.showAt(position[0], position[1]+25);
    },
    
    editGroup: function(me) {
    	var form = this.groupWin.down('form').getForm();
    	form.loadRecord(this.selectrecord);
    	this.groupWin.showAt(me.getPosition());
    },
    
    createRightPanel: function() {
    	  var userstore = Ext.create('Ext.data.Store', {
    		pageSize: 25,
    	    fields:['id', 'name', 'sex', 'openid', 'headimgurl', 'subscribe', 'nickname', 'subscribe_time', 'remark', 'modifyDate'],
    	    proxy: {
    	  	    type: 'ajax',
    	        url: '/admin/wxUser.do?list', 
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
    	        }
    	    },
            autoLoad: true
    	});
    	
    	var sex = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{"text":"全部", "value": ""},{"text":"男", "value": "1"},{"text":"女", "value": "2"},{"text":"未知", "value": "0"}]
    	});
    	
    	var selModel = Ext.create('Ext.selection.CheckboxModel', {
	        listeners: {
	            selectionchange: function(sm, selections) {
	            	Ext.getCmp('WxUser_Add').setValue("");
	            	Ext.getCmp('WxUser_Add').setDisabled(selections.length == 0);
	                Ext.getCmp('WxUser_MoveGroup').setDisabled(selections.length == 0);
	            }
	        }
	    });
    	
    	this.usergrid = Ext.create('Ext.grid.Panel', {
    	    store: userstore,
    	    region: 'center',
    	    selModel:selModel,
    	    columnLines: true,
    	    dockedItems: [{
    	        xtype: 'pagingtoolbar',
    	        store: userstore,  
    	        dock: 'bottom',
    	        displayInfo: true
    	    }],
    	    columns: [{
    	    	text: '序号',
    	    	width: 45,
    	    	xtype: 'rownumberer'
    	    }, { 
    	    	text: '微信昵称',  
    	    	dataIndex: 'nickname',
    	    	flex: 0.15
    	    }, {
    	    	text: '头像',
    	    	dataIndex: 'headimgurl',
    	    	flex: 0.06,
    	    	renderer: function(v, meta){
    	    		meta.tdAttr = "data-qtip='<img src=\"" + v + "\" width=128 heigtht=128/>'";
    	    		return '<img src="'+ v + '" width=45 heigtht=45/>';
    	    	}
    	    }, {
    	    	text: '性别',
    	    	dataIndex: 'sex',
    	    	flex: 0.05,
    	    	renderer: function(v) {
                  if (v == 1) return "<font color=blue>男</font>";
                  if (v == 2) return "<font color=red>女</font>";
                  else return "<font color=green>未知</font>";
                }
    	    }, {
    	    	text: '是否成为粉丝',
    	    	dataIndex: 'subscribe',
    	    	flex: 0.1,
    	    	renderer: function(v) {
                  if (v == 1) return "<font color=green>是</font>";
                  else return "<font color=red>否</font>";
                }
    	    }, {
    	    	text: '关注时间',
    	    	dataIndex: 'subscribe_time',
    	    	flex: 0.15,
    	    	renderer: function(v){
    	    		if (v == '') return "--";
    	    		return v;
 	            }
    	    }, {
    	    	text: '更新日期',
    	    	dataIndex: 'modifyDate',
    	    	flex: 0.15,
    	    	renderer: function(v){
    	    		if (v == null) return "--";
    	    		return Ext.Date.format(new Date(v.time), 'Y-m-d H:i:s');
 	            }
    	    }, {
    	    	text: '备注名',
    	    	dataIndex: 'remark',
    	    	flex: 0.20,
    	    	renderer: function(v){
    	    		if (v == '') return "--";
    	    		return v;
 	            }
    	    }, {
            	xtype: 'actiontextcolumn',
            	text: '主要操作',
            	flex: 0.20,
                items : [{  
                    text : "修改备注",  
                    cls: 'icon-edit', 
                    scope: this,
                    handler: this.setRemark
                }/*, {  
                    text : "删除",  
                    cls: 'icon-del',
                    scope: this,
                    handler: this.delGrid
                }*/]  
            }],
            tbar: [{
            	xtype:"container",
            	width: '100%',
            	items: [{
            	   xtype:"toolbar",
                   border: '0 0 1 0',
            	   items: [{ 
		            	xtype: 'button', 
		            	text: '刷新',
		            	iconCls: 'icon-refresh',
		            	scope: this,
		            	handler: this.refreshGird
		            }, '-', {
		            	id: 'WxUser_Add',
		            	xtype: 'combo',
	    	        	name: 'name',
	    	        	disabled: true,
	    	        	store: this.groupstore,
	    	        	emptyText: '添加到',
	    	            queryMode: 'local',
	    	            valueField: 'groupId',
	    	            displayField: 'name',
	    	            editable: false,
	    	            listeners: {
	    	            	expand: function(me) {
	    	            		
	    	            	}
	    	            }
		            }, '-', {
		            	id: 'WxUser_MoveGroup',
		            	xtype: 'button', 
		            	text: '移动到分组',
		            	tooltip:'选择中的将全部移动到选择的分组',
		            	scope: this,
		            	disabled: true,
		            	handler: this.moveGroup
		            }, '->', { 
		            	xtype: 'button', 
		            	text: '授权设置',
		            	iconCls: 'icon-config',
		            	scope: this,
		            	handler: this.configWx
		            }, {
		            	xtype: 'button', 
		            	text: '同步微信粉丝',
		            	iconCls: 'icon-refresh',
		            	scope: this,
		            	handler: this.synchronizeWx
		            }]
            	}, {
            		xtype: 'form',
		            frame: true,
		            style: 'border: none;',
			    	fieldDefaults: {
			            labelWidth: 60,
			            labelAlign: 'right',
			            anchor: '100%',
			            margin: '0 0 0 20',
			            labelStyle: 'color:green;padding-left: 4px'
			        },
			        items: [{
			        	xtype: 'fieldset',
			        	title: '高级查询',
			        	layout: 'anchor',
		    			collapsible: true,
		    			border: false,
		    			items: [{
		    				xtype: 'buttongroup',
		    				columns: 6,
		    				floatable:true,
		    				defaults: {  
                            	scale: 'small'  
                        	},
                        	items: [{
                        		xtype : 'textfield',  
	                            name: 'nickname',
	                            emptyText: '',
	                            fieldLabel : '微信昵称'
                        	},{
			    	        	xtype: 'datefield',
			    	        	name: 'subscribestart',
			    	        	anchor: '100%',
			    	        	fieldLabel: '关注日期',
			    	        	format: 'Y-m-d'
			    	        }, {
			    	        	xtype: 'datefield',
			    	        	name: 'subscribend',
			    	        	anchor: '100%',
			    	        	labelWidth: 20,
			            		labelAlign: 'left',
			            		margin: '0 0 0 0',
			    	        	fieldLabel: '至',
			    	        	format: 'Y-m-d',
			    	        	value: Ext.util.Format.date(new Date(),'Y-m-d')
			    	        }, {
			    	        	xtype: 'combo',
			    	        	name: 'sex',
			    	        	fieldLabel: '性　　别',
			    	        	store: sex,
			    	        	emptyText: '全部',
			    	            queryMode: 'local',
			    	            valueField: 'value',
			    	            displayField: 'text',
			    	            editable: false
			    	        }, {
			    	        	xtype: 'button',
			    	        	text: '开始查询',
			    	        	scope: this,
			    	        	iconCls: 'icon-search',
			    	        	handler: this.searchGrid
			    	        }, {
			    	        	xtype: 'button',
			    	        	text: '重置查询条件',
			    	        	scope: this,
			    	        	handler: this.resetGrid
			    	        }]
		    			}]
			        }]
            	}]
            }]
    	});
    	
    	return this.usergrid;
    },
    
    createWindows: function() {
    	  this.contextMenu = Ext.create('Ext.menu.Menu', {
    		items: [{
	            text: '重命名',
	            cls: 'icon-edit',
	            scope: this,
	            handler: this.editGroup
	        }, {
	        	text: '删除',
	            cls: 'icon-del',
	            scope: this,
	            handler: this.delDataGroup
	        }]
    	});
    	
    	this.groupWin = Ext.create('Ext.window.Window', {
    	    //title: '编辑分组信息',
    	    height: 100,
    	    width: 250,
    	    border: 0,
    	    layout: 'border',
    	    //modal: true,
    	    resizable: false,
    	    header: false,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 15,
    	    	fieldDefaults: {
    	            labelWidth: 65,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'name',
    	            fieldLabel: '分组名称',
    	            allowBlank: false
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitFormGroup
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.groupWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    	
    	this.remarkWin = Ext.create('Ext.window.Window', {
    	    title: '修改备注信息',
    	    height: 150,
    	    width: 250,
    	    border: 0,
    	    layout: 'border',
    	    modal: true,
    	    resizable: false,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 20,
    	    	fieldDefaults: {
    	            labelWidth: 65,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'remark',
    	            fieldLabel: '备注信息',
    	            emptyText: '限制30字内',
    	            allowBlank: false
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitFormData
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.remarkWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    	
    	 this.configWin = Ext.create('Ext.window.Window', {
    	    title: '设置授权信息',
    	    height: 210,
    	    width: 480,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	fieldDefaults: {
    	            labelWidth: 75,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'appId',
    	            fieldLabel: 'AppId',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'appSecret',
    	            fieldLabel: 'AppSecret',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'component',
	        		width: '100%',
	        		margin: '0 0 5 80',
					html: '<a href="https://mp.weixin.qq.com" target="view_window">参数如何获取?点击登陆微信公众号官方平台->开发->基本配置</a>'
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitFormConfig
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.configWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    	
    	this.progressbar = Ext.create('Ext.window.Window', {
    		header: false,
    		resizable: false,
    		border: 0,
    	    width: 400,
			minHeight: 20,
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{
    	    	xtype: 'progressbar',
    	    	width:400
    	    }],
    	    listeners: {
    	    	show: function(win, eOpts) {
    	    		   win.down('progressbar').wait({
					   interval: 200, //速度
					   //duration: 3000,//区间
					   increment: 15,
					   text: '正在同步数据，请稍后...',
					   animate: true,
					   scope: this,
					   fn: function(){
					   		this.items.get(0).updateProgress(1, '完成!', true);
					        this.hide();
					   }
					});
    	    	}
    	    }
    	});
    },
    
    refreshGird: function() {
    	this.usergrid.getStore().reload();
    },
    
    resetGrid: function() {
    	var form  = this.usergrid.down('form').getForm();
    	form.reset();
    },
    
    searchGrid: function() {
    	var form  = this.usergrid.down('form').getForm();
    	var store = this.usergrid.getStore();
    	var subscribe_time = new Array();
    	subscribe_time.push(Ext.Date.format(form.findField('subscribestart').getValue(), 'Y-m-d'));
    	subscribe_time.push(Ext.Date.format(form.findField('subscribend').getValue(), 'Y-m-d'));
    	store.currentPage = 1;
    	store.proxy.extraParams = {
			nickname: form.findField('nickname').getValue(),
			sex: form.findField('sex').getValue(),
			subscribe_time: subscribe_time
		};
		store.load();
    },
    
    setRemark: function(grid, rowIndex, colIndex) {
    	var form = this.remarkWin.down('form');
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    	form.getForm().findField('remark').setValue(rec.get('nickname'));
    	this.remarkWin.setTitle("正在为 " + rec.get('nickname') + " 设置备注名称");
    	this.remarkWin.show();
    },
    
    submitFormData: function(fn) {
    	var grid = this.usergrid;
    	var form = fn.up('form').getForm();
    	var win = fn.up('form').up('window');
    	form.submit({
    		url: '/admin/wxUser.do?update',
    	    success: function(form, action) {
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
    	       grid.getStore().reload();
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    submitFormGroup: function() {
    	var grid = this.groupgrid.down('dataview').getStore();
    	var win = this.groupWin;
    	var form = win.down('form').getForm();
    	form.submit({
    		url: '/admin/wxUser.do?saveGroup',
    	    success: function(form, action) {
    	       win.hide();
    	       grid.reload();
    	       Ext.Msg.alert('提示', action.result.title);
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    delDataGroup: function() {
    	var grid = this.groupgrid.down('dataview').getStore();
    	var id = this.selectrecord.get('id');
    	Ext.MessageBox.confirm('确认', '删除该分组不可恢复，请确认', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/wxUser.do?deleteGroup',
        	    params: {
        	        id: id
        	    },
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	if (res.success) {
        	    		grid.reload();
        	    	}
        	    	Ext.Msg.alert('提示', res.title);
        	    },
        	    failure: function(response) {
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    }
        	});
		});
    },
    
    moveGroup: function() {
    	var store = this.usergrid.getStore();
    	var selections = this.usergrid.getSelectionModel( ).getSelection();
		var groupid = Ext.getCmp('WxUser_Add').getValue();
		if(groupid === "") return false;
    	var openids = "";
    	for (var i = 0; i < selections.length; i++) {
	        	var record = selections[i];
	        	openids += record.get('openid');
	        	if(selections.length - i >1) openids += ",";
		}
		Ext.MessageBox.confirm('确认', '选中的会员将会移动到该分组，请确认', function(btn) {
			if (btn != 'yes') return;
			Ext.Ajax.request({
	    	    url: '/admin/wxUser.do?batchMoveGroup',
	    	    params: {
	    	    	openid_list: openids,
	    	    	to_groupid: groupid
	    	    },
	    	    success: function(response){
	    	    	var res = Ext.decode(response.responseText);
	    	    	if (res.success) {
	    	    		store.reload();
	    	    	}
	    	    	Ext.Msg.alert('提示', res.title);
	    	    },
	    	    failure: function(response) {
	    	    	var res = Ext.decode(response.responseText);
	    	    	Ext.Msg.alert('提示', res.title);
	    	    }
	    	});
		});
    },
    
    configWx: function() {
    	var form = this.configWin.down('form');
    	this.configWin.show();
    	form.getForm().reset();
    	Ext.Ajax.request({
    	    url: '/admin/diymen.do?wxinfo',
    	    success: function(response){
    	    	var res = Ext.decode(response.responseText);
	    		form.getForm().findField('appId').setValue(res.appId);
	    		form.getForm().findField('appSecret').setValue(res.appSecret);
    	    },
    	    failure: function(response) {
    	    	var res = Ext.decode(response.responseText);
    	    	Ext.Msg.alert('提示', res.title);
    	    }
    	});
    },
    
    submitFormConfig: function() {
    	var form = this.configWin.down('form');
    	var win = this.configWin;
    	form.submit({
    		url: '/admin/diymen.do?wxconfig',
    	    success: function(form, action) {
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    synchronizeWx: function() {
    	var grid = this.usergrid;
    	var progressbar = this.progressbar;
    	Ext.MessageBox.confirm('确认', '如果您的粉丝很多将会消耗较长时间,是否继续?', function(btn) {
    		if (btn != 'yes') return;
    		progressbar.show();
    		Ext.Ajax.request({
        	    url: '/admin/wxUser.do?synchronizeWx',
        	    timeout: 100000000,
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	if (res.success) {
        	    		grid.getStore().reload();
        	    	}
        	    	progressbar.hide();
        	    	Ext.Msg.alert('提示', res.title);
        	    },
        	    failure: function(response) {
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    }
        	});
		});
    },
 
    onDestroy: function(){
    	this.configWin.destroy();
    	this.groupWin.destroy();
    	this.contextMenu.destroy();
    	this.progressbar.destroy();
        this.callParent(arguments);
    }

});