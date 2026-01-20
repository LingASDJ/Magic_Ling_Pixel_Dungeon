return {
  version = "1.10",
  luaversion = "5.1",
  tiledversion = "1.11.2",
  class = "",
  orientation = "orthogonal",
  renderorder = "right-down",
  width = 9,
  height = 9,
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
      width = 9,
      height = 9,
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
        1, 1, 1, 5, 5, 5, 67, 1, 1,
        67, 145, 145, 49, 49, 49, 145, 145, 67,
        67, 145, 49, 49, 74, 49, 49, 145, 1,
        5, 49, 49, 1, 5, 1, 49, 49, 5,
        5, 49, 5, 5, 21, 5, 5, 59, 5,
        5, 49, 49, 1, 5, 1, 49, 49, 5,
        1, 145, 49, 49, 5, 49, 49, 145, 1,
        67, 145, 145, 49, 49, 49, 145, 145, 67,
        1, 67, 1, 5, 5, 5, 1, 1, 1
      }
    }
  }
}
