Ext.define('SystemApp.View.Info',{
	extend:'Ext.panel.Panel',
	xtype: 'info',
	
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
    	    fields:['name','content','discript','level','visible', 'markcode'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/info.do?list', 
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
    	    columnLines: true,
    	    dockedItems: [{
    	        xtype: 'pagingtoolbar',
    	        store: store,  
    	        dock: 'bottom',
    	        displayInfo: true
    	    }],
    	    columns: [{ 
    	    	text: '信息名称',  
    	    	dataIndex: 'name',
    	    	flex: 0.25
    	    }, {
    	    	text: '等级排序',
    	    	dataIndex: 'level',
    	    	flex: 0.15
    	    }, {
    	    	text: '是否显示',
    	    	dataIndex: 'visible',
    	    	flex: 0.15,
    	    	renderer: function(v) {
                	if (v) return "<font color=green>显示</font>";
                	else return "<font color=red>隐藏</font>";
                }
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
            	id: 'Info.SearchName'
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
			name: Ext.getCmp('Info.SearchName').getValue()
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
    	    	
    	this.editWin = Ext.create('Ext.window.Window', {
    	    title: '编辑信息',
    	    height: 600,
    	    width: 900,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	autoScroll: true,
    	    	fieldDefaults: {
    	            labelWidth: 90,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        },{
    	        	xtype: 'textfield',
    	            name: 'name',
    	            fieldLabel: '信息名称',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'numberfield',
    	        	name: 'level',
    	        	fieldLabel: '等级排序',
    	            minValue: 0,
    	            maxValue: 1000,
    	            allowBlank: false,
    	            value: 0
    	        }, {
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
    	        	xtype: 'textarea',
    	        	name: 'discript',
    	        	fieldLabel: '描述',
    	        	height: 250
    	        }, {
    	        	xtype: 'kindeditor',
    	        	name: 'content',
    	        	fieldLabel: '内容',
    	        	height: 250
    	        }],
    	        buttons: [{ 
    	        	text: '保存',
    	        	scope: this,
    	        	handler: this.submitFormData
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
    	this.classifyStore.reload();
    },
    
    editGrid: function(grid, rowIndex, colIndex) {
    	var form = this.editWin.down('form');
    	this.editWin.show();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    },
    
    submitFormData: function() {
    	var grid  = this.grid;
    	var form = this.editWin.down('form');
    	var win = this.editWin;
    	form.submit({
    		url: '/admin/info.do?save',
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
    
    delGrid: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var grid  = this.grid;
		Ext.MessageBox.confirm('确认', '确认删除该信息', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/info.do?delete',
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
    }
    
	
});