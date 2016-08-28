Ext.define('SystemApp.View.Module', {
    extend: 'Ext.panel.Panel',
    xtype: 'module',
    
    initComponent: function() {
    	
    	Ext.apply(this, {
    		border: 0,
            layout: 'border',
            items: [this.createGrid()]
        });
    	this.createEditWindows();
        this.callParent(arguments);
    },
    
    
    createGrid: function() {
    	var store = Ext.create('Ext.data.Store', {
    		pageSize: 25,
    	    fields:['name', 'modifyDate', 'visible', 'level', 'isdefault'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/module.do?list', 
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
    	        }
    	    },
            autoLoad: true
    	});
    	
    	this.grid = Ext.create('Ext.grid.Panel', {
    		border: 0,
    	    store: store,
    	    region: 'center',
    	    dockedItems: [{
    	        xtype: 'pagingtoolbar',
    	        store: store,  
    	        dock: 'bottom',
    	        displayInfo: true
    	    }],
    	    columns: [{ 
    	    	text: '模块名称',  
    	    	dataIndex: 'name',
    	    	flex: 0.25
    	    }, {
    	    	text: '最后修改时间',
    	    	dataIndex: 'modifyDate',
    	    	flex: 0.25,
    	    	renderer: function(v){
    	    		if (v == null) return "";
    	    		return Ext.Date.format(new Date(v.time), 'Y-m-d H:i');
 	            }
    	    }, {
    	        text: '是否显示',
    	    	dataIndex: 'visible',
    	    	flex: 0.15,
    	    	renderer: function(v) {
                	if (v) return "<font color=green>显示</font>";
                	else return "<font color=red>隐藏</font>";
                }
    	    }, {
    	    	text: '排序等级',  
    	    	dataIndex: 'level',
    	    	flex: 0.25
    	    }, {
            	xtype: 'actiontextcolumn',
            	text: '主要操作',
            	flex: 0.25,
                items : [{  
                    text : "编辑",  
                    cls: 'icon-edit', 
                    scope: this,
                    handler: this.editGrid
                }, {  
                    text : "删除",  
                    cls: 'icon-del',
                    scope: this,
                    handler: this.delGrid
                }]  
            }],
            tbar: [{ 
            	xtype: 'button', 
            	text: '刷新',
            	iconCls: 'icon-refresh',
            	scope: this,
            	handler: this.refreshGird
            }, { 
            	xtype: 'button', 
            	text: '添加',
            	iconCls: 'icon-add',
            	scope: this,
            	handler: this.addGrid
            }, '->' ,{
            	xtype: 'textfield',
            	width: 180,
            	emptyText: '输入名称搜索',
            	id: 'Module.SearchName'
            },{ 
            	xtype: 'button', 
            	text: '搜索',
            	iconCls: 'icon-search',
            	scope: this,
            	handler: this.searchGrid
            }]
    	});
    	
    	return this.grid;
    },
    
    refreshGird: function() {
    	this.grid.getStore().reload();
    },
    
    searchGrid: function() {
    	var store = this.grid.getStore();
    	store.currentPage = 1;
    	store.proxy.extraParams = {
			name: Ext.getCmp('Module.SearchName').getValue()
		};
		store.load();
    },
    
    createEditWindows: function() {
    	
    	var enabled = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{
    	    	"text":"显示", "value": true
    	    },{
    	    	"text":"隐藏", "value": false
    	    }]
    	});
    	
    	var store = Ext.create('Ext.data.ArrayStore', {
	        fields: ['id','name'],
	        proxy: {
    	        type: 'ajax',
    	        url: '/admin/admin.do?role'
    	    },
    	     autoLoad: true
	    });
    	
    	this.editWin = Ext.create('Ext.window.Window', {
    	    title: '编辑系统模块',
    	    height: 500,
    	    width: 700,
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
		            anchor: '100%',
		            labelStyle: 'color:green;padding-left:4px'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'hiddenfield',
    	        	name: 'isdefault',
    	        	value: 0
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 20,
		        	items: [{
	    	        	xtype: 'textfield',
	    	            name: 'name',
	    	            allowBlank: false,
	    	            fieldLabel: '模块名称'
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　输入显示模块的名称</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 20,
		        	items: [{
	    	        	xtype: 'combo',
	    	        	name: 'visible',
	    	        	fieldLabel: '是否显示',
	    	        	store: enabled,
	    	            queryMode: 'local',
	    	            valueField: 'value',
	    	            displayField: 'text',
	    	            editable: false,
	    	            allowBlank: false
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　设置是否显示该模块</span>'
		        	}]
    	        }, {
    	        	xtype:"container",
		        	layout: 'column',
		        	margin: 20,
		        	items: [{
	    	        	xtype: 'numberfield',
	    	            name: 'level',
	    	            minValue: 0,
	    	            value: 0,
	    	            fieldLabel: '排序等级'
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa">　影响模块显示顺序，数值越小显示位置越靠前</span>'
		        	}]
    	        }, {
    	        	xtype: 'itemselector',
		            name: 'roles',
		            anchor: '100%',
		            fieldLabel: '分配角色',
		            store: store,
		            displayField: 'name',
		            valueField: 'id',
		            value: [],
		            buttonsText: {top: "移到最上层", up: "向上移动", add: "选中该项", remove: "删除该项", down: "向下移动", bottom: "移到最低层"},
		            msgTarget: 'side',
		            fromTitle: '未选的角色',
		            toTitle: '已选的角色'
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitFormDate
    	        }, {
    	        	text: '清除所有角色',
    	        	scope: this,
    	        	handler: this.clearSelected
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
    
    addGrid: function() {
		var form = this.editWin.down('form');
    	this.editWin.show();
    	form.getForm().reset();
    },
    
    editGrid: function(grid, rowIndex, colIndex) {
    	var form = this.editWin.down('form');
    	this.editWin.show();
    	form.getForm().reset();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    	var roles = [];
    	Ext.Ajax.request({
    	    url: '/admin/module.do?role',
    	    params: {
    			moduleId: rec.get('id')
    		},
    	    success: function(response){
    	    	var res = Ext.decode(response.responseText);
    	    	for (var i = 0; i < res.length; i++) {
    	    		if(res[i].checked){
    	    			roles[i] = res[i].id;
    	    		}
    	    	}
    	    	form.getForm().findField('roles').setValue( roles );
    	    },
    	    failure: function(response) {
    	    	Ext.Msg.alert('提示', '操作异常');
    	    }
    	});
    },
    
    submitFormDate: function() {
    	var grid  = this.grid;
    	var form = this.editWin.down('form');
    	var win = this.editWin;
    	form.submit({
    		url: '/admin/module.do?save',
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
    
    clearSelected: function() {
    	var field = this.editWin.down('form').getForm().findField('roles');
    	if(!field.disabled){
    		field.clearValue();
    	}
    },
    
    delGrid: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var grid  = this.grid;
    	if(record.get('isdefault')) {Ext.Msg.alert('提示', "系统默认模块，不能删除");return;}
		Ext.MessageBox.confirm('确认', '是否删除该模块?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/module.do?delete',
        	    params: {
        	        id: record.get('id')
        	    },
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	if (res.success) {
        	    		grid.getStore().reload();
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
    
		onDestroy: function(){
			this.editWin.destroy();
			this.callParent(arguments);
		}

});