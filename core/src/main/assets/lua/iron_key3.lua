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
        1, 1, 67, 67, 1, 1, 67, 67, 67,
        67, 81, 81, 81, 59, 81, 81, 81, 67,
        1, 81, 73, 73, 1, 73, 73, 81, 67,
        5, 81, 73, 1, 1, 1, 73, 81, 5,
        5, 81, 67, 11, 21, 11, 67, 81, 5,
        5, 81, 67, 11, 11, 11, 67, 81, 5,
        1, 81, 73, 67, 67, 67, 73, 81, 67,
        67, 81, 81, 81, 81, 81, 81, 81, 1,
        67, 67, 1, 67, 67, 1, 67, 67, 1
      }
    }
  }
}
