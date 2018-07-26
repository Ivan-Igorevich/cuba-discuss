# README
* Simple commenting module (Cuba-platform application extension)

### What is this repository for?
* This repository is a open-source extension for a Haulmont CUBA-platform, which allows you to simply add (view and reply) comments to certain entities. This repository is distributed "as is". There's no versions or official documentation (website) for this module, yet.

### How do I get set up?
* All the above is written in CUBA Platform 6.8.1. Please, make sure you have it's dependencies, or change the version in `build.gradle` file.

* To set this repo up and running in your CUBA-application, you should simply extend your app (in project properties) from this one. Download latest release, import it into CUBA Studio, create an app-component and Install app-component from 'Run' menu. Then open your app, go to project properties and add this repo as an extension by pressing a '+' button in 'Custom components' group.

* Platform version of this repository is 6.6.1 so be sure that your app supports it. No additional dependencies, configurations or database settings needed (not yet revealed).

The most casual way to add comments to your app is to add comments frame to any of your entities viewer/editor screens go to it's XML file and add a frame:

```XML
<frame id="comments" screen="discuss$Comment.browse" width="50%"/>
```

Where `width` is an optional custom parameter, and make sure to add a following code to a controller:

```java
@Override
public void ready() {
    String dbName = metadata.getSession().getClassNN(getItem().getClass()).getName();
    comments.initialize(isNew)
            .setCurrentUser(userSession.getUser())
            .setCurrentEntityId(getItem().getId())
            .setCurrentEntityName(dbName)
            .applyAndShow();
    super.ready();
}
```

Where
`comments` is injected comments frame identifier;
`metadata` is injected MetaData interface variable;
`isNew` is a boolean, which indicates, if current editor view is opened for a new entity or not. If there's no need to hide comments for new (created) entities, simply provide `false` to initializer. The most common way to know if the item is new - set `isNew` to true in method `@Override protected void initNewItem(T item)`
And `userSession` is an injected UserSession interface.

The second way is to add comments to your entity browser screen, so that you will need to provide information about the entity, you're about to attach comments to. The simplest way is to add a listener to DataSource changes of an entity browse table. In this case the XML will look the same as in the previous part, but the controller should consider re-initializing on every entity change. For example, if we are connecting comments to an entity, which is listed in a `groupTable`, we should add a `datasource` listener, which will initialize comments frame every time you select an item. And don't forget to add an initial screen hiding.

```java
    public void ready() {
        super.ready();
        comments.initialize(false)
                .setFrameVisible(false);
        testsDs.addItemChangeListener(e -> {
            Test item = e.getItem();
            if (item == null) return;
            String dbName = metadata.getSession().getClassNN(item.getClass()).getName();
            comments.initialize(false)
                    .setCurrentUser(userSession.getUser())
                    .setCurrentEntityId(item.getId())
                    .setCurrentEntityName(dbName)
                    .applyAndShow();
        });
    }
```




### Contribution guidelines
* Feel free to contribute to this repo, especially if you found bugs;
* Feel free to make code-reviews;
* Feel free to use this repo as you want.

### Who do I talk to?
* Ivan I. Ovchinnikov
* [Personal Telegram](https://t.me/WayneShephard)
* [Personal website](https://iovchinnikov.ru)
