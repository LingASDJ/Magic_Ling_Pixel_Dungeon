return {
  version = "1.10",
  luaversion = "5.1",
  tiledversion = "1.11.2",
  class = "",
  orientation = "orthogonal",
  renderorder = "right-down",
  width = 9,
  height = 10,
  tilewidth = 16,
  tileheight = 16,
  nextlayerid = 2,
  nextobjectid = 1,
  properties = {},
  tilesets = {
    {
      name = "tiles_ghost",
      firstgid = 1,
      filename = "tiles_ghost.tsx"
    },
    {
      name = "water_ghost",
      firstgid = 257,
      class = "",
      tilewidth = 32,
      tileheight = 32,
      spacing = 0,
      margin = 0,
      columns = 1,
      image = "../古堡素材优化/environment/water_ghost.png",
      imagewidth = 32,
      imageheight = 32,
      objectalignment = "unspecified",
      tilerendersize = "tile",
      fillmode = "stretch",
      tileoffset = {
        x = 0,
        y = 0
      },
      grid = {
        orientation = "orthogonal",
        width = 32,
        height = 32
      },
      properties = {},
      wangsets = {},
      tilecount = 1,
      tiles = {}
    }
  },
  layers = {
    {
      type = "tilelayer",
      x = 0,
      y = 0,
      width = 9,
      height = 10,
      id = 1,
      name = "图块层 1",
      class = "",
      visible = true,
      opacity = 1,
      offsetx = 0,
      offsety = 0,
      parallaxx = 1,
      parallaxy = 1,
      properties = {},
      encoding = "lua",
      data = {
        67, 67, 1, 67, 1, 1, 1, 1, 1,
        67, 49, 49, 49, 49, 49, 49, 49, 67,
        1, 49, 67, 5, 21, 5, 67, 49, 1,
        25, 49, 67, 49, 5, 49, 67, 49, 25,
        25, 49, 25, 49, 5, 49, 25, 49, 25,
        25, 49, 25, 49, 5, 49, 25, 49, 25,
        25, 49, 67, 1, 5, 1, 67, 49, 25,
        1, 49, 67, 49, 5, 49, 67, 49, 1,
        1, 49, 49, 49, 59, 49, 49, 49, 67,
        67, 67, 1, 5, 5, 5, 1, 67, 67
      }
    }
  }
}
