{
  "startPoint": {
    "x": 56,
    "y": 8,
    "heading": "linear",
    "startDeg": 90,
    "endDeg": 180,
    "locked": false
  },
  "lines": [
    {
      "id": "line-5jzag6wnu5c",
      "name": "Intake Far Balls",
      "endPoint": {
        "x": 11.80926411748748,
        "y": 34.842679781724605,
        "heading": "linear",
        "startDeg": 90,
        "endDeg": 180
      },
      "controlPoints": [
        {
          "x": 53.42691117631101,
          "y": 33.68125707310626
        }
      ],
      "color": "#7B6665",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mr24geon-54y6bd",
      "name": "Go and shoot far balls",
      "endPoint": {
        "x": 43.98870145266026,
        "y": 8.353319870541922,
        "heading": "constant",
        "reverse": false,
        "degrees": 180,
        "startDeg": 0,
        "endDeg": 0
      },
      "controlPoints": [],
      "color": "#7B6665",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": false
    },
    {
      "id": "mr24hwy0-t5dauq",
      "name": "Cycle 1",
      "endPoint": {
        "x": 9.173062841600856,
        "y": 8.265886157221807,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [],
      "color": "#7B6665",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": false
    },
    {
      "id": "mr25al2d-2h35wo",
      "name": "Path 4",
      "endPoint": {
        "x": 43.94195905592797,
        "y": 8.323527934939351,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [],
      "color": "#7B6665",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    }
  ],
  "shapes": [
    {
      "id": "triangle-1",
      "name": "Red Goal",
      "vertices": [
        {
          "x": 141.5,
          "y": 70
        },
        {
          "x": 141.5,
          "y": 141.5
        },
        {
          "x": 120,
          "y": 141.5
        },
        {
          "x": 135.67715458276336,
          "y": 118.41928864569083
        },
        {
          "x": 135.67715458276334,
          "y": 69.41928864569083
        }
      ],
      "color": "#dc2626",
      "fillColor": "#ff6b6b"
    },
    {
      "id": "triangle-2",
      "name": "Blue Goal",
      "vertices": [
        {
          "x": 6,
          "y": 119
        },
        {
          "x": 25,
          "y": 141.5
        },
        {
          "x": 0,
          "y": 141.5
        },
        {
          "x": 0,
          "y": 70
        },
        {
          "x": 6,
          "y": 70
        }
      ],
      "color": "#2563eb",
      "fillColor": "#60a5fa"
    }
  ],
  "sequence": [
    {
      "kind": "path",
      "lineId": "line-5jzag6wnu5c"
    },
    {
      "kind": "path",
      "lineId": "mr24geon-54y6bd"
    },
    {
      "kind": "wait",
      "id": "mr24gxm6-oucmb1",
      "name": "Wait",
      "durationMs": 500,
      "locked": true
    },
    {
      "kind": "path",
      "lineId": "mr24hwy0-t5dauq"
    },
    {
      "kind": "wait",
      "id": "mr24ughj-mbu6fl",
      "name": "Wait",
      "durationMs": 500,
      "locked": true
    },
    {
      "kind": "path",
      "lineId": "mr25al2d-2h35wo"
    }
  ],
  "pathChains": [
    {
      "id": "chain-mping3aq-8xfoth",
      "name": "Main Chain",
      "color": "#7B6665",
      "lineIds": [
        "mr24geon-54y6bd",
        "mr24hwy0-t5dauq",
        "mr25al2d-2h35wo"
      ]
    }
  ],
  "settings": {
    "xVelocity": 75,
    "yVelocity": 65,
    "aVelocity": 3.141592653589793,
    "kFriction": 0.1,
    "rWidth": 17.461,
    "rHeight": 15.468,
    "safetyMargin": 1,
    "maxVelocity": 40,
    "maxAcceleration": 30,
    "maxDeceleration": 30,
    "fieldMap": "decode.webp",
    "robotImage": "/robot.png",
    "theme": "auto",
    "showGhostPaths": false,
    "showOnionLayers": false,
    "onionLayerSpacing": 3,
    "onionColor": "#dc2626",
    "onionNextPointOnly": false,
    "showHeadingArrow": false,
    "headingArrowLength": 50,
    "headingArrowColor": "#ffffff",
    "headingArrowThickness": 2,
    "pathOpacity": 1
  },
  "version": "1.2.1",
  "timestamp": "2026-07-01T14:05:32.853Z"
}