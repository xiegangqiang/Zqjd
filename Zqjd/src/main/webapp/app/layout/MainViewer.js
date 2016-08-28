Ext.define('SystemApp.App', {
    extend: 'Ext.container.Viewport',
    
    initComponent: function(){
        
        Ext.define('Menu', {
            extend: 'Ext.data.Model',
            fields: ['name', 'namespace', 'img']
        });

        Ext.apply(this, {
            layout: {
                type: 'border'
            },
            items: [this.createTopPanel(), this.createFeedPanel(), this.createFeedInfo()/*, this.createBottonPanel()*/]
        });
        
        //初始化界面
        //this.onMenuSelect(this, "扫描进度", 'scanning');
        this.callParent(arguments);
    },
    
    createFeedPanel: function(){
        this.leftPanel = Ext.create('widget.layout.leftpanel', {
            region: 'west',
            collapsible: true,
            width: 225,
            split: true,
            //collapsed: true,
            minWidth: 175,
            id: 'LeftPanel.View',
            listeners: {
                scope: this,
                menuselect: this.onMenuSelect
            },
            dockedItems: [{
			    xtype: 'toolbar',
			    layout: 'fit',
			    dock: 'top',
			    items: [{
			  		xtype: 'dataview',
		    	    region: 'center',
		    	    cls: 'modulecls',
    				autoScroll: true,
    				scrollable: null,
    				store: Ext.create('Ext.data.Store', {
			    	    fields: ['id', 'name', 'img'],
			    	    proxy: {
			    	        type: 'ajax',
			    	        url: '/admin/module.do?module'
			    	    },
			            autoLoad: true
			    	}),
		    	    tpl: new Ext.XTemplate(
					    '<tpl for=".">',
					        '<div class="thumb-wrap">',
					          '<img src="{img}" />',
					          '<span>{name}</span>',
					        '</div>',
					    '</tpl>'
					),
		    	    itemSelector: 'div.thumb-wrap',
    	            listeners: {
	                    scope: this,
	                    itemclick: function(me, record, item, index, e, eOpts) {
	                    	Ext.getCmp('MainPanel.View').removeAll();
	                    	Ext.getCmp('LeftPanel.View').initAndLoadView('/admin/menu.do?tree&module=' + record.get('id'));
	                    }
    	            }
			    }]
			}]
        });
        
        return this.leftPanel;
    },
    
    createFeedInfo: function(){
        this.mainPanel = Ext.create('widget.layout.mainpanel', {
        	id: 'MainPanel.View',
            region: 'center',
            minWidth: 300
        });
        return this.mainPanel;
    },
    
    createTopPanel: function() {
    	var alias = 'widget.layout.toppanel';
    	var className = Ext.ClassManager.getNameByAlias(alias);
        var ViewClass = Ext.ClassManager.get(className);
        return new ViewClass();
    },
    
    createBottonPanel: function() {
    	var alias = 'widget.layout.bottompanel';
    	var className = Ext.ClassManager.getNameByAlias(alias);
        var ViewClass = Ext.ClassManager.get(className);
        return new ViewClass();
    },
    
    onMenuSelect: function(menu, name, nspace){
    	//this.leftPanel.collapse('left', true);
        this.mainPanel.addPanel(name, nspace);
    }
});
