return {
  version = "1.10",
  luaversion = "5.1",
  tiledversion = "1.11.2",
  class = "",
  orientation = "orthogonal",
  renderorder = "right-down",
  width = 11,
  height = 11,
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
    }
  },
  layers = {
    {
      type = "tilelayer",
      x = 0,
      y = 0,
      width = 11,
      height = 11,
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
        5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
        5, 49, 49, 49, 50, 49, 50, 49, 49, 49, 5,
        5, 49, 5, 74, 5, 21, 5, 74, 5, 49, 5,
        1, 49, 74, 25, 5, 5, 5, 25, 74, 49, 67,
        1, 49, 25, 25, 25, 5, 25, 25, 25, 49, 67,
        67, 49, 25, 74, 25, 5, 25, 74, 25, 49, 1,
        67, 49, 74, 25, 25, 5, 25, 25, 74, 49, 1,
        1, 49, 25, 49, 25, 5, 25, 49, 25, 49, 67,
        1, 49, 25, 74, 25, 5, 25, 74, 25, 49, 1,
        1, 49, 49, 49, 49, 84, 49, 49, 49, 49, 1,
        1, 67, 1, 1, 5, 5, 5, 1, 1, 67, 67
      }
    }
  }
}
