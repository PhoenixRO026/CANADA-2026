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
      "id": "line-c7t0rjfik6u",
      "name": "Path 1",
      "endPoint": {
        "x": 11.279578606158838,
        "y": 35.082658022690424,
        "heading": "tangential",
        "startDeg": 90,
        "endDeg": 180,
        "reverse": false
      },
      "controlPoints": [
        {
          "x": 53.426661264181526,
          "y": 36.77876823338735
        }
      ],
      "color": "#9DCCD8",
      "locked": true,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mr24gelh-4qygol",
      "name": "Path 2",
      "endPoint": {
        "x": 42,
        "y": 8,
        "heading": "linear",
        "reverse": false,
        "degrees": 0,
        "startDeg": 180,
        "endDeg": 180
      },
      "controlPoints": [
        {
          "x": 19.782005585292048,
          "y": 13.975737197657587
        }
      ],
      "color": "#9DCCD8",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": true
    },
    {
      "id": "mr2525x1-4arsap",
      "name": "Path 3",
      "endPoint": {
        "x": 9,
        "y": 8,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [],
      "color": "#9DCCD8",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": true
    },
    {
      "id": "mr2542du-9a1hfr",
      "name": "Path 4",
      "endPoint": {
        "x": 42,
        "y": 8,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [],
      "color": "#9DCCD8",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": true
    },
    {
      "id": "mr25bw10-itiqxp",
      "name": "Path 5",
      "endPoint": {
        "x": 9,
        "y": 26,
        "heading": "linear",
        "reverse": false,
        "startDeg": 180,
        "endDeg": 180
      },
      "controlPoints": [
        {
          "x": 40.11345218800648,
          "y": 26.52674230145867
        }
      ],
      "color": "#9DCCD8",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": true
    },
    {
      "id": "mr25ftxt-wyxtz9",
      "name": "Path 6",
      "endPoint": {
        "x": 42,
        "y": 8,
        "heading": "linear",
        "reverse": false,
        "startDeg": 180,
        "endDeg": 180
      },
      "controlPoints": [
        {
          "x": 19.977309562398702,
          "y": 13.725283630470019
        }
      ],
      "color": "#9DCCD8",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": true
    },
    {
      "id": "mr25i2br-6qawza",
      "name": "Path 7",
      "endPoint": {
        "x": 9,
        "y": 8,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [],
      "color": "#9DCCD8",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": true
    }
  ],
  "shapes": [
    {
      "id": "triangle-1",
      "name": "Red Goal",
      "vertices": [
        {
          "x": 141.7293354943274,
          "y": 66.78930307941653
        },
        {
          "x": 141.04132901134523,
          "y": 141.5
        },
        {
          "x": 115.41329011345219,
          "y": 140.58265802269042
        },
        {
          "x": 134.33063209076172,
          "y": 114.64262560777959
        },
        {
          "x": 131.11993517017828,
          "y": 66.55996758508914
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
          "x": 0.2293354943273906,
          "y": 64.72528363047002
        },
        {
          "x": 11.045380875202593,
          "y": 65.4132901134522
        }
      ],
      "color": "#2563eb",
      "fillColor": "#60a5fa"
    }
  ],
  "sequence": [
    {
      "kind": "path",
      "lineId": "line-c7t0rjfik6u"
    },
    {
      "kind": "path",
      "lineId": "mr24gelh-4qygol"
    },
    {
      "kind": "path",
      "lineId": "mr2525x1-4arsap"
    },
    {
      "kind": "path",
      "lineId": "mr2542du-9a1hfr"
    },
    {
      "kind": "path",
      "lineId": "mr25bw10-itiqxp"
    },
    {
      "kind": "path",
      "lineId": "mr25ftxt-wyxtz9"
    },
    {
      "kind": "path",
      "lineId": "mr25i2br-6qawza"
    }
  ],
  "pathChains": [
    {
      "id": "chain-mr246wpv-8ce952",
      "name": "Main Chain",
      "color": "#9DCCD8",
      "lineIds": [
        "line-c7t0rjfik6u",
        "mr24gelh-4qygol",
        "mr2525x1-4arsap",
        "mr2542du-9a1hfr",
        "mr25bw10-itiqxp",
        "mr25ftxt-wyxtz9",
        "mr25i2br-6qawza"
      ]
    }
  ],
  "version": "1.2.1",
  "timestamp": "2026-07-01T14:13:00.217Z"
}