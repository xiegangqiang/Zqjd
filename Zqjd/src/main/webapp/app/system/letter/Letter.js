Ext.define('SystemApp.View.Letter', {
    extend: 'Ext.panel.Panel',
    xtype: 'letter',
    
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
    	    fields:['id', 'modifyDate', 'ranged', 'content', 'isSend', 'visit', 'omit'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/letter.do?list', 
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
    	        }
    	    },
            autoLoad: true
    	});
    	
    	var selModel = Ext.create('Ext.selection.CheckboxModel', {
	        listeners: {
	            selectionchange: function(sm, selections) {
	                Ext.getCmp('Letter_RemoveAll').setDisabled(selections.length == 0);
	            }
	        }
	    });
    	
    	this.grid = Ext.create('Ext.grid.Panel', {
    		border: 0,
    	    store: store,
    	    region: 'center',
    	    selModel: selModel,
    	    columnLines: true,
    	    dockedItems: [{
    	        xtype: 'pagingtoolbar',
    	        store: store,  
    	        dock: 'bottom',
    	        displayInfo: true
    	    }],
		    plugins: [{
		        ptype: 'rowexpander',
		        rowBodyTpl : new Ext.XTemplate(
		            '<span style="color: green;margin-left: 70px">{usergrade:this.formatObject}</span>',
		            '<span style="color: green;">{estpgrade:this.formatObject}　</span>',
		            '<span style="color: green;">已被 {visit:this.format} 人查看，</span>',
		            '<span style="color: green;">被 {omit:this.format} 人删除</span>',
		        {
		        	format: function(v) {
		        		return '<span style="color: red;">' + v + '</span>';
		        	},
		            formatObject: function(v){
		            	if(v == null) return ""; 
		            	return '( ' + v.name + ' )';
		            }
		        })
		    }],
    	    columns: [{xtype: 'rownumberer'}, { 
    	    	text: '发送群体',  
    	    	dataIndex: 'ranged',
    	    	flex: 0.1,
    	    	renderer: function(v) {
    	    		if (v == 0) return "所有用户";
    	    	}
    	    }, {
    	    	text: '发送内容',
    	    	dataIndex: 'content',
    	    	flex: 0.7
    	    }, {
    	    	text: '发送时间',
    	    	dataIndex: 'modifyDate',
    	    	flex: 0.2,
    	    	renderer: function(v){
    	    		if (v == null) return "--";
    	    		return Ext.Date.format(new Date(v.time), 'Y-m-d H:i:s');
 	            }
    	    }, {
    	    	text: '状态',
    	    	dataIndex: 'isSend',
    	    	flex: 0.1,
    	    	renderer: function(v){
    	    		switch (v) {
    	    			case 0: return '<span style="color: red">未发送</span>';break;
    	    			case 1: return '<span style="color: green">已发送</span>';break;
    	    			case 2: return '<span style="">未知状态</span>';break;
    	    		}
 	            }
    	    }, {
            	xtype: 'actiontextcolumn',
            	text: '主要操作',
            	flex: 0.3,
                items : [{  
                    text : "编辑",  
                    cls: 'icon-edit', 
                    scope: this,
                    handler: this.editGrid
                }, {
                	text : "发送",  
                    cls: 'icon-sendMsg', 
                    scope: this,
                    handler: this.sendGrid
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
            }, '-', { 
            	xtype: 'button', 
            	text: '添加站内信',
            	iconCls: 'icon-add',
            	scope: this,
            	handler: this.addGird
            }, '-', {
	   	        id: 'Letter_RemoveAll',
            	xtype: 'button', 
            	text: '批量删除',
            	iconCls: 'icon-del',
            	tooltip:'选择中的将全部删除',
            	scope: this,
            	disabled: true,
            	handler: this.delallGrid
            }]
    	});
    	
    	return this.grid;
    },
    
    refreshGird: function() {
    	this.grid.getStore().reload();
    },
    
    createEditWindows: function() {
    	
    	this.editWin = Ext.create('Ext.window.Window', {
    	    title: '编辑站内信',
    	    height: 370,
    	    width: 750,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	autoScroll: true,
    	    	fieldDefaults: {
    	            labelWidth: 50,
    	            labelAlign: 'left',
    	            anchor: '100%',
    	            labelStyle: 'color:green;padding-left:4px'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
		        	xtype:"container",
		        	layout: 'column',
		        	items: [{
	    	        	xtype: 'kindeditor',
	    	            name: 'content',
	    	            fieldLabel: '内容',
	    	            allowBlank: false,
	    	            height: 250
	    	        }, {
		        		xtype: 'component',
						html: '<span style="color:#aaa"></span>'
		        	}]
		        }],
    	        buttons: [{ 
    	        	text: '确定',
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
    
    addGird: function() {
    	var form = this.editWin.down('form').getForm();
    	this.editWin.show();
    	form.reset();
    },
    
    editGrid: function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	if(rec.get('isSend') === 0) {
	    	var form = this.editWin.down('form');
	    	this.editWin.show();
	    	form.loadRecord(rec);
    	}else Ext.Msg.alert('提示', '已发送的信息不能修改');
    },
    
    sendGrid: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var grid  = this.grid;
    	if(record.get('isSend') === 0) {
			Ext.MessageBox.confirm('发送站内信', '确认要发送此信息吗？', function(btn) {
	    		if (btn != 'yes') return;
	    		Ext.Ajax.request({
	        	    url: '/admin/letter.do?send',
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
    	}else Ext.Msg.alert('提示', '信息已发送');
    },
    
    submitFormData: function() {
    	var grid  = this.grid;
    	var form = this.editWin.down('form');
    	var win = this.editWin;
    	form.submit({
    		url: '/admin/letter.do?save',
    	    success: function(form, action) {
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
    	       grid.getStore().reload();
    	    },
    	    failure: function(form, action) {
    	    	if(action.result != null){
    	    		Ext.Msg.alert('提示', action.result.title);
    	    	}
    	    }
    	});
    },
    
    delGrid: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var grid  = this.grid;
		Ext.MessageBox.confirm('确认', '是否删除该站内信?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/letter.do?delete',
        	    params: {
        	        ids: record.get('id') + ","
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
    
    delallGrid: function() {
    	var store = this.grid.getStore();
    	var selections = this.grid.getSelectionModel( ).getSelection();
    	var ids = '';
	    	for (var i = 0; i < selections.length; i++) {
		        	var record = selections[i];
		        	ids += record.get('id') + ',';
			}
	       Ext.MessageBox.confirm('确认', '此操作将会全部删除所选项目，请确认!', function(btn) {
			if (btn != 'yes') return;
			Ext.Ajax.request({
	    	    url: '/admin/letter.do?delete',
	    	    params: {
	    	    	ids: ids
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
    
    onDestroy: function(){
    	this.editWin.destroy();
        this.callParent(arguments);
    }

});