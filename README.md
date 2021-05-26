`gradle test`

### `ParenthesisParser.parse("'ok'")`
```
└── <start>
    └── S
        ├── ['
        │   └── Single quote ([']ok)
        ├── S
        │   └── AnyText ('[ok]')
        └── ']
            └── Single quote (ok['])
```

### `ParenthesisParser.parse("'ok',")`
```
└── <start>
    └── S
        ├── S
        │   ├── ['
        │   │   └── Single quote ([']ok)
        │   ├── S
        │   │   └── AnyText ('[ok]')
        │   └── ']
        │       └── Single quote (before comma) (ok['],)
        └── S
            └── AnyText ('[,])
```

### `ParenthesisParser.parse(" 'ok',")`
```
└── <start>
    └── S
        ├── S
        │   ├── S
        │   │   └── AnyText ([ ]')
        │   └── S
        │       ├── ['
        │       │   └── Single quote (after space) ( [']ok)
        │       ├── S
        │       │   └── AnyText ('[ok]')
        │       └── ']
        │           └── Single quote (before comma) (ok['],)
        └── S
            └── AnyText ('[,])
```